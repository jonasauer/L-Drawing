package main.java.application;

import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.styles.*;
import com.yworks.yfiles.view.*;
import com.yworks.yfiles.view.input.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import main.java.algorithm.LDrawing;

public class GUIController {

    @FXML
    private BorderPane borderPane_main;
    @FXML
    private GraphControl graphControl;
    @FXML
    private Button button_undo;
    @FXML
    private Button button_redo;
    @FXML
    private ToggleButton toggleButton_showGrid;
    @FXML
    private ToggleButton toggleButton_Snapping;
    @FXML
    private ToggleButton toggleButton_orthogonalEdges;
    @FXML
    private ToggleButton toggleButton_LDrawing;

    private IGraph graph;

    private GridVisualCreator grid;
    private GraphSnapContext graphSnapContext;
    private LabelSnapContext labelSnapContext;

    private static int nodes = 1;


    @FXML
    void initialize(){

        //init graph control... has to be done here because scene builder doesn't support graphControl
        this.graphControl = new GraphControl();
        this.borderPane_main.setCenter(graphControl);
        this.graph = graphControl.getGraph();
        this.graph.setUndoEngineEnabled(true);

        initializeUserInteraction();
        initializeSnapping();
        initializeGrid();
        initializeIOInteractions();
        initializeStyle();
    }

    void onLoaded(){

        graphControl.fitGraphBounds();
    }

    private void initializeUserInteraction() {

        GraphEditorInputMode graphEditorInputMode = new GraphEditorInputMode();
        graphEditorInputMode.setGroupingOperationsAllowed(true);
        graphEditorInputMode.addNodeCreatedListener((source, args) -> {
            graph.addLabel(args.getItem(), "" + nodes++);
        });

        graphControl.setInputMode(graphEditorInputMode);
    }

    private void initializeSnapping() {

        GraphEditorInputMode graphEditorInputMode = (GraphEditorInputMode) graphControl.getInputMode();
        if (graphEditorInputMode != null) {
            graphSnapContext = new GraphSnapContext();
            graphSnapContext.setGridSnapType(GridSnapTypes.ALL);
            graphSnapContext.setEnabled(toggleButton_Snapping.isSelected());
            labelSnapContext = new LabelSnapContext();
            labelSnapContext.setEnabled(toggleButton_Snapping.isSelected());
            graphEditorInputMode.setSnapContext(graphSnapContext);
            graphEditorInputMode.setLabelSnapContext(labelSnapContext);
        }
    }

    private void initializeGrid() {

        GridInfo gridInfo = new GridInfo();
        gridInfo.setHorizontalSpacing(50);
        gridInfo.setVerticalSpacing(50);

        grid = new GridVisualCreator(gridInfo);
        grid.setPen(Pen.getLightGray());
        graphControl.getBackgroundGroup().addChild(grid, ICanvasObjectDescriptor.ALWAYS_DIRTY_INSTANCE);

        graphSnapContext.setNodeGridConstraintProvider(new GridConstraintProvider<>(gridInfo));
        graphSnapContext.setBendGridConstraintProvider(new GridConstraintProvider<>(gridInfo));

        setGridVisible(toggleButton_showGrid.isSelected());
    }

    private void initializeIOInteractions(){

        graphControl.setFileIOEnabled(true);
    }

    private void initializeStyle(){

        ShapeNodeStyle defaultShapeNodeStyle = new ShapeNodeStyle();
        defaultShapeNodeStyle.setShape(ShapeNodeShape.ROUND_RECTANGLE);
        defaultShapeNodeStyle.setPaint(Color.ORANGE);
        defaultShapeNodeStyle.setPen(Pen.getWhite());
        this.graphControl.getGraph().getNodeDefaults().setStyle(defaultShapeNodeStyle);

        PolylineEdgeStyle edgeStyle = new PolylineEdgeStyle();
        edgeStyle.setSourceArrow(new Arrow(ArrowType.CIRCLE, Color.WHITE));
        edgeStyle.setTargetArrow(new Arrow(ArrowType.DEFAULT, Color.WHITE));
        edgeStyle.setPen(Pen.getWhite());
        this.graphControl.getGraph().getEdgeDefaults().setStyle(edgeStyle);
    }

    private boolean isGridVisible() {

        return toggleButton_showGrid.isSelected();
    }

    private void setGridVisible(boolean visible) {

        if (grid != null)
            grid.setVisible(visible);

        if (graphSnapContext != null)
            graphSnapContext.setGridSnapType(visible ? GridSnapTypes.ALL : GridSnapTypes.NONE);

        graphControl.invalidate();
    }

    @FXML
    public void handleLDrawing(){

        new LDrawing().lDrawing(graph);
    }

    @FXML
    public void handleSnapping(){

        if (graphSnapContext != null)
            graphSnapContext.setEnabled(toggleButton_Snapping.isSelected());

        if (labelSnapContext != null)
            labelSnapContext.setEnabled(toggleButton_Snapping.isSelected());
    }

    @FXML
    void handleGrid() {

        setGridVisible(isGridVisible());
    }

    @FXML
    void handleOrthogonal() {
        if (graphControl.getInputMode() instanceof GraphEditorInputMode) {
            GraphEditorInputMode graphEditorInputMode = (GraphEditorInputMode) graphControl.getInputMode();
            OrthogonalEdgeEditingContext context = new OrthogonalEdgeEditingContext();
            context.setEnabled(toggleButton_orthogonalEdges.isSelected());
            graphEditorInputMode.setOrthogonalEdgeEditingContext(context);
        }
    }

    @FXML
    void handleAbout() {
        //TODO: implement me
    }

    @FXML
    void handleCenterViewPort() {

        graphControl.fitGraphBounds();
    }

    @FXML
    void handleClose() {

        Platform.exit();
    }

    @FXML
    void handleCopy() {

        ICommand.COPY.execute(null, graphControl);
    }

    @FXML
    void handleCut() {

        ICommand.CUT.execute(null, graphControl);
    }

    @FXML
    void handleDelete() {

        ICommand.DELETE.execute(null, graphControl);
    }

    @FXML
    void handleOpen() {

        ICommand.OPEN.execute(null, graphControl);
    }

    @FXML
    void handlePaste() {

        ICommand.PASTE.execute(null, graphControl);
    }

    @FXML
    void handleRedo() {

        ICommand.REDO.execute(null, graphControl);
        button_undo.setDisable(!graph.getUndoEngine().canUndo());
        button_redo.setDisable(!graph.getUndoEngine().canRedo());
    }

    @FXML
    void handleResetZoom() {

        graphControl.setZoom(1.0);
    }

    @FXML
    void handleSave() {

        ICommand.SAVE.execute(null, graphControl);
    }

    @FXML
    void handleSaveAs() {

        ICommand.SAVE_AS.execute(null, graphControl);
    }

    @FXML
    void handleSelectAll() {

        ICommand.SELECT_ALL.execute(null, graphControl);
    }

    @FXML
    void handleUndo() {

        ICommand.UNDO.execute(null, graphControl);
        button_undo.setDisable(!graph.getUndoEngine().canUndo());
        button_redo.setDisable(!graph.getUndoEngine().canRedo());
    }

    @FXML
    void handleZoomIn() {

        graphControl.setZoom(graphControl.getZoom() / 1.25);
    }

    @FXML
    void handleZoomOut() {

        graphControl.setZoom(graphControl.getZoom() * 1.25);
    }
}
