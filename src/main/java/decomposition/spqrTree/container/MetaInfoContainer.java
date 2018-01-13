package main.java.decomposition.spqrTree.container;

import java.util.HashMap;

public class MetaInfoContainer {

    private HashMap<MetaInfo, Object> map;


    public MetaInfoContainer() {
        map = new HashMap<MetaInfo, Object>();
    }


    public Object getMetaInfo(MetaInfo name) {
        if (map.containsKey(name))
            return map.get(name);
        return null;
    }


    public void setMetaInfo(MetaInfo name, Object content) {
        map.put(name, content);
    }
}
