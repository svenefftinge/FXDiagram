package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot

import static extension de.fxdiagram.core.extensions.ForeachExtensions.*

class SelectAllAction implements DiagramAction {
	
	override perform(XRoot root) {
		root.diagram.allShapes.forEachExt[if(selectable) selected = true]
	}
}