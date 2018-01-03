package main.java.decomposition.hyperGraph;

import java.util.Collection;


public interface IEntityModel<E extends IEntity> {

	public Collection<E> getEntities();
	
}
