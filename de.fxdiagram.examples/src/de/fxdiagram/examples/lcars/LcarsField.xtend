package de.fxdiagram.examples.lcars

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.tools.ChooserConnectionProvider
import de.fxdiagram.core.tools.actions.LayoutAction
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.lib.tools.CoverFlowChooser
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.examples.lcars.LcarsExtensions.*
import static extension javafx.util.Duration.*

class LcarsField extends Parent {

	FlowPane flowPane
	
	LcarsNode node
	
	Node queryProgress
	
	new(LcarsNode node, String name, String value) {
		this.node = node
		val ChooserConnectionProvider connectionProvider = [ 
			host,  choice, choiceInfo | 
			new XConnection(host, choice, name) => [
				sourceArrowHead = new TriangleArrowHead(it, true)
				new XConnectionLabel(it) => [
					text.text = name.replace('_', ' ')
				]
			]
		]
		children += flowPane = new FlowPane => [
			prefWrapLength = 150
			children += new Text => [
				text = name.replace('_', ' ') + ": "
				font = lcarsFont(12)
				fill = FLESH
			]
			var currentWord = ''
			for (c : value.toCharArray) {
				currentWord = currentWord + c
				if (c.isSplitHere) {
					children += new Text(currentWord) => [
						font = lcarsFont(12)
						fill = ORANGE
					]
					currentWord = ''
				}
			}
			if (!currentWord.empty) {
				children += new Text(currentWord) => [
					font = lcarsFont(12)
					fill = ORANGE
				]
			}
			onMousePressed = [ event |
				allTextNodes.forEach[fill = RED]
				queryProgress = createQueryProgress;
				(lcarsNode.node as Pane).children += queryProgress
				if(event.button != MouseButton.PRIMARY) {
					val Service<Void> service = [|
						new LcarsQueryTask(this, name, value, connectionProvider)
					]
					service.start
				}
			]
			onMouseReleased = [
				if(button == MouseButton.PRIMARY) {
					val newConnections  = diagram.nodes
						.filter(LcarsNode)
						.filter[
							it != node && data.get(name)==value 
							&& !outgoingConnections.exists[target == node && key == name]
							&& !incomingConnections.exists[source == node && key == name]
						]
						.map[connectionProvider.getConnection(node, it, null)]
						.toList
					diagram.connections += newConnections
					if(!newConnections.empty)
						new LayoutAction().perform(root)
					resetVisuals
				}
			]
		]
	}
	
	def getLcarsNode() {
		node
	}

	protected def getAllTextNodes() {
		flowPane.children.filter(Text)
	}

	protected def isSplitHere(char c) {
		Character.isWhitespace(c)
	}
	
	def addAnimation(Timeline timeline) {
		timeline => [
			for(textNode: allTextNodes) {
				for(index: 0..<textNode.text.length) {
					keyFrames += new KeyFrame(
						cycleDuration.add(15.millis), 
						new KeyValue(textNode.textProperty, textNode.text.substring(0, index + 1))
					)			
				}
				textNode.text = ''
			}
		]
		timeline
	}
	
	def resetVisuals() {
		if(queryProgress != null) {
			(lcarsNode.node as Pane).children -= queryProgress
			queryProgress = null			
		}
		allTextNodes.head.fill = FLESH
		allTextNodes.tail.forEach[fill = ORANGE]
	}
	
	def createQueryProgress() {
		val label = new Text
		val node = new RectangleBorderPane => [
			children += label => [
				text = 'querying...'
				font = lcarsFont(42)
				fill = ORANGE
				StackPane.setMargin(it, new Insets(5,5,5,5))
			]
			backgroundPaint = Color.BLACK
			backgroundRadius = 10
			borderWidth = 0
		]
		new Timeline => [
			keyFrames += new KeyFrame(0.millis, new KeyValue(label.opacityProperty, 0))
			keyFrames += new KeyFrame(700.millis, new KeyValue(label.opacityProperty, 1))
			keyFrames += new KeyFrame(750.millis, new KeyValue(label.opacityProperty, 1))
			keyFrames += new KeyFrame(770.millis, new KeyValue(label.opacityProperty, 0))
			cycleCount = -1
			play
		]
		node
	}
}

class LcarsQueryTask extends Task<Void> {
	
	LcarsField host
	String fieldName
	String fieldValue
	ChooserConnectionProvider connectionProvider
	
	new(LcarsField host, String fieldName, String fieldValue, ChooserConnectionProvider connectionProvider) {
		this.host = host
		this.fieldName = fieldName
		this.fieldValue = fieldValue
		this.connectionProvider = connectionProvider
	}
	
	override protected call() throws Exception {
		val siblings = host.lcarsDiagram.lcarsAccess.query(fieldName, fieldValue)
		val lcarsNode = host.lcarsNode
		val chooser = new CoverFlowChooser(lcarsNode, Pos.BOTTOM_CENTER)
		chooser.connectionProvider = connectionProvider
		val alreadyConnected = 
			(host.lcarsNode.incomingConnections.filter[key == fieldName].map[source] 
				+ host.lcarsNode.outgoingConnections.filter[key == fieldName].map[target])
			.filter(LcarsNode).map[dbId].toSet
		alreadyConnected.add(lcarsNode.dbId)
		siblings
			.filter[!alreadyConnected.contains(get('_id').toString)]
			.forEach[it, i | 
				chooser.addChoice(new LcarsNode(it) => [
					width = lcarsNode.width
					height = lcarsNode.height
				])
			]
		Platform.runLater [|
			host.root.currentTool = chooser
			host.resetVisuals
		]
		null
	}
	
}
