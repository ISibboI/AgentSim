package de.isibboi.agentsim.game.entities.ai.knowledge;

import com.google.common.collect.Multiset;

/**
 * A multiset of categories this object belongs to.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface CategoryMultiset extends Multiset<Category>, CategorySet {

}
