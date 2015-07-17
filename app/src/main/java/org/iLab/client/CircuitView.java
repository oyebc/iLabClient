package org.iLab.client;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CircuitView extends View {

	// Non Verbose coordinates
	private final int Ay[] = { 63, 84, 147, 167, 189, 212, 235, 302, 323, 346,
			368, 390, 457, 479 };
	private final int By[] = { 71, 93, 154, 178, 200, 221, 244, 310, 333, 354,
			377, 399, 466, 488 };
	private final int Ax1[] = { 191, 216, 237, 260, 283, 330, 354, 377, 400,
			424, 471, 492, 516, 538, 563, 608, 631, 654, 677, 700, 746, 770,
			792, 814, 836 };
	private final int Bx1[] = { 199, 223, 246, 269, 293, 339, 363, 386, 409,
			431, 479, 502, 525, 548, 571, 616, 639, 663, 686, 708, 756, 778,
			801, 824, 847 };
	private final int Ax2[] = { 172, 195, 219, 242, 266, 291, 314, 337, 361,
			384, 408, 431, 455, 478, 501, 523, 548, 572, 594, 619, 642, 666,
			689, 713, 734, 759, 781, 805, 829, 851 };
	private final int Bx2[] = { 183, 204, 230, 252, 275, 299, 323, 345, 368,
			392, 415, 439, 463, 487, 511, 535, 557, 581, 604, 628, 650, 674,
			696, 719, 744, 768, 790, 815, 836, 859 };
	// private final int Ax3[]= { 171, 195, 220, 243, 267, 291, 312, 336, 360,
	// 384, 406, 431, 453, 477, 501, 524, 547, 572, 595, 618, 641, 664, 689,
	// 712, 734, 759, 779, 804, 826, 851 }
	private final int Ax4[] = { 185, 211, 234, 258, 281, 326, 350, 374, 397,
			420, 467, 491, 514, 538, 562, 608, 631, 655, 678, 700, 747, 770,
			793, 817, 839 };
	private final int Bx4[] = { 197, 220, 243, 267, 289, 336, 360, 385, 409,
			431, 477, 501, 524, 548, 572, 617, 641, 665, 688, 712, 756, 780,
			803, 826, 850 };

	private final Node fixedNodesArray[] = { new Node("n00"), new Node("n01"),
			new Node("n02"), new Node("n03"), new Node("n04"), new Node("n05"),
			new Node("n06"), new Node("n07"), new Node("n08"), new Node("n09"),
			new Node("n10"), new Node("n11"), new Node("n12"), new Node("n13"),
			new Node("n14"), new Node("n15"), new Node("n16"), new Node("n17"),
			new Node("n18"), new Node("n19"), new Node("n20"), new Node("n21"),
			new Node("n22"), new Node("n23"), new Node("n24"), new Node("n25"),
			new Node("n26"), new Node("n27"), new Node("n28"), new Node("n29"),
			new Node("n30"), new Node("n31"), new Node("n32"), new Node("n33"),
			new Node("n34"), new Node("n35"), new Node("n36"), new Node("n37"),
			new Node("n38"), new Node("n39"), new Node("n40"), new Node("n41"),
			new Node("n42"), new Node("n43"), new Node("n44"), new Node("n45"),
			new Node("n46"), new Node("n47"), new Node("n48"), new Node("n49"),
			new Node("n50"), new Node("n51"), new Node("n52"), new Node("n53"),
			new Node("n54"), new Node("n55"), new Node("n56"), new Node("n57"),
			new Node("n58"), new Node("n59"), new Node("n60"), new Node("n61"),
			new Node("n62"), new Node("n63")

	};
	private ArrayList<Node> resistorNodes;
	private float allowedY = -1;
	// value in above matrices closest to value of point clicked
	private float allowedX = -1;

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mBitmapPaint;
	private String debug;
	private float dwidth;
	private float dheight;
	float offsetx;
	float offsety;

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 16;
	private int LabConfiguration = 0;
	private boolean hasTouchStarted = false;
	private Bitmap pixel;
	private Bitmap tip;
	private Bitmap tip2;
	private Bitmap tip3;
	private Bitmap tip4;
	private Node startNode;
	private Node currentNode;
	private Bitmap scaledbb;
	private Path mPath;
	private Paint mPaint;
	// strictly for drawing path on touch move
	private float moveX;
	private float moveY;

	private Bitmap resistor1;
	// resistor or cable
	private int currentComponent;
	// resistorCount is no of resistors allowed
	// resistorNo is present no of resistors
	private final int resistorCount = 4;
	private int resistorNo = 0;
	// hasDrawn = has any resistor been drawn?
	private boolean hasDrawn = false;
	private boolean hasMoved = false;
	private boolean moveRight;
	private String debug2 = "";
	// connectionMode =0, deleteMode =1
	private int bbMode = 0;
	private Bitmap deleteBitmap;
	private Canvas deleteCanvas;
	private Edge currentEdge;

	public CircuitView(Context context, AttributeSet attrs) {
		super(context, attrs);

		resistorNodes = new ArrayList<Node>();
		// mBitmap = Bitmap.createBitmap(1024, 600, Bitmap.Config.ARGB_8888);

		debug = "";
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(getResources().getColor(R.color.textViewColor));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		currentComponent = 0;

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
		dwidth = ((1.2f * w * 1.0f) / 1024.0f);
		dheight = 1.0f * h / 524.0f;
		mBitmap = Bitmap.createBitmap((int) (1.2f * w), h,
				Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		deleteBitmap = Bitmap.createBitmap((int) (1.2f * w), h,
				Bitmap.Config.ARGB_8888);
		deleteCanvas = new Canvas(deleteBitmap);
		debug = w + " " + dheight;
		Bitmap bb = BitmapFactory.decodeResource(getResources(),
				R.drawable.bbcircuit1);
		pixel = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.pixel), (int) (8 * dwidth),
				(int) (8 * dheight), true);
		mPaint.setStrokeWidth((float) (4 * dwidth));
		tip = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.tip), (int) (8 * dwidth),
				(int) (8 * dheight), true);
		tip2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.tip2), (int) (8 * dwidth),
				(int) (8 * dheight), true);
		tip3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.tip3), (int) (8 * dwidth),
				(int) (8 * dheight), true);
		tip4 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.tip4), (int) (8 * dwidth),
				(int) (8 * dheight), true);
		scaledbb = Bitmap.createScaledBitmap(bb, 12 * w / 10, h, true);
		resistor1 = Bitmap.createScaledBitmap((BitmapFactory.decodeResource(
				getResources(), R.drawable.resistor1k2)),
				(int) (90 * dwidth / 1.2), (int) (18 * dheight), true);

		offsetx = (w - scaledbb.getWidth()) / 2f;
		offsety = -1.0f * h / 20f;

		for (int i = 0; i < Edge.graphEdges.size(); i++) {
			Edge edge = Edge.graphEdges.get(i);
			drawEdges(edge.getStartX(), edge.getStartY(), edge.getEndX(), edge
					.getEndY());
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint background = new Paint();
		background.setColor(getResources()
				.getColor(R.color.overview_background));
		Paint grid = new Paint();
		grid.setColor(0xFF000000);
		// draw white background
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		// draw breadboard in background
		// (getHeight()-scaledbb.getHeight())/2;
		// debug = offsetx + " "+ offsety;
		canvas.drawBitmap(scaledbb, offsetx, offsety, null);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint);

		canvas.drawText(debug, 100, 75, grid);
		// debug ="";

		canvas.drawText(debug2, 100, 90, grid);
		
		canvas.drawText(resistorNo +" "+ resistorCount, 100, 105, grid);

		// int [] nX,nX2;

		// for (int i=0;i<Ay.length;i++)
		// {
		// if(i==0||i==1)
		// {
		// nX = Ax1;
		// nX2 = Bx1;
		// }
		// else if(i==Ay.length-1||i==Ay.length-2)
		// {
		// nX = Ax4;
		// nX2= Bx4;
		// }
		// else
		// {
		// nX = Ax2;
		// nX2 = Bx2;
		// }
		// for(int j=0;j<nX.length;j++)
		// {
		// canvas.drawRect(new RectF( (nX[j]*dwidth + offsetx),
		// (Ay[i]*dheight+offsety), (nX2[j]*dwidth + offsetx),
		// (By[i]*dheight+offsety)), mPaint);
		// }
		// }

		// if (bbMode==1)
		// canvas.drawBitmap(deleteBitmap, 0,0, mPaint);
	}

	private void touch_start(float x, float y) {

		switch (currentComponent) {
		case 0:
			mPath.reset();
			if (checkXYCoordinates(x, y)) {
				mPath.moveTo(allowedX, allowedY);
				mX = allowedX;
				mY = allowedY;
				hasTouchStarted = true;
				startNode = currentNode;
				debug = "Node " + startNode.getName();
				moveX = allowedX + pixel.getWidth() / 4;
				moveY = allowedY + pixel.getHeight() / 4;
			}
			break;

		case 1:
			if (resistorNo < resistorCount)
				if (checkXYCoordinates(x, y)) {
					mX = allowedX;
					mY = allowedY;
					hasTouchStarted = true;
					startNode = currentNode;
					// resistorNo++;
					// debug = "Node " + startNode.getName();

				}
			break;

		}
	}

	private void touch_move(float x, float y) {

		switch (currentComponent) {
		case 0:
			if (hasTouchStarted) {

				float dx = Math.abs(x - mX);
				float dy = Math.abs(y - mY);
				if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
					mPath
							.quadTo(moveX, moveY, (x + moveX) / 2,
									(y + moveY) / 2);
					moveX = x;
					moveY = y;
				}
			}
			break;

		case 1:

			if (resistorNo < resistorCount)
				if (hasTouchStarted) {
					if (!hasDrawn) {
						float dx = x - mX;
						// float dy = Math.abs(y - mY);

						if (Math.abs(dx) >= TOUCH_TOLERANCE) {

							if (dx > 0) 
								moveRight = true;
							else
								moveRight=false;
							
								getResistorCoordinates(mX, mY);
								mCanvas.drawBitmap(resistor1, mX
										+ pixel.getWidth() / 4, mY
										- pixel.getWidth() / 4, null);
								currentNode = fixedNodesArray[Integer
										.parseInt(startNode.getName()
												.substring(1)) + 3];

								// else
								{
									// mCanvas.drawBitmap(resistor1, mX +
									// pixel.getWidth()/4-resistor1.getWidth(),mY-pixel.getWidth()/4,
									// null);
									// currentNode =
									// fixedNodesArray[Integer.parseInt(startNode.getName().substring(1))-3];

								}
								resistorNo++;
								hasDrawn = true;
								hasMoved = true;
								resistorNodes.add(startNode);
								resistorNodes.add(currentNode);
								Edge.graphEdges
										.add(new Edge(
												startNode,
												currentNode,
												startNode.getName() + "-"
														+ currentNode.getName(),
												new float[] {
														mX + pixel.getWidth()
																/ 4,
														mY - pixel.getWidth()
																/ 4,
														mX
																+ pixel
																		.getWidth()
																+ resistor1
																		.getWidth(),
														mY
																+ (pixel
																		.getWidth() / 4)
																+ resistor1
																		.getHeight() },
												1));
								// debug += startNode.getName() + " to " +
								// currentNode.getName();
							

						}
					}
				}

			break;
		}
	}

	private void touch_up(float x, float y) {

		mPath.reset();

		/*
		 * if ( hasTouchStarted) { if(checkXYCoordinates(x, y)) { //
		 * mPath.lineTo(mX, mY); // draw on offscreen buffer
		 * mCanvas.drawPath(mPath, mPaint);
		 * 
		 * } // kill this so as not double draw mPath.reset(); }
		 */
		switch (currentComponent) {
		case 0:
			if (hasTouchStarted)
				if (checkXYCoordinates(x, y)) {
					float dx = Math.abs(allowedX - mX);
					float dy = Math.abs(allowedY - mY);
					int xsquares = (int) (dx / pixel.getWidth());
					int ysquares = (int) (dy / pixel.getHeight());
					float startptx = Math.min(allowedX, mX) + pixel.getWidth()
							/ 2;
					float startpty = Math.min(allowedY, mY) + pixel.getHeight()
							/ 2;

					if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {

						if (xsquares >= ysquares) {
							mCanvas.drawBitmap(tip, startptx, mY
									+ pixel.getWidth() / 4, null);
							mCanvas.drawBitmap(tip2, startptx + dx
									- (tip2.getWidth()), mY + pixel.getWidth()
									/ 4, null);
							for (int i = 1; i < xsquares - 1; i++)
								mCanvas.drawBitmap(pixel, startptx
										+ (i * pixel.getWidth()), mY
										+ pixel.getWidth() / 4, null);

							startNode.addEdge(currentNode, startNode.getName()
									+ "-" + currentNode.getName(), new float[] {
									startptx, mY + pixel.getWidth() / 4,
									startptx + dx,
									mY + pixel.getWidth() * 5 / 4 });
							// currentNode.addEdge(startNode,
							// currentNode.getName()+"-"+startNode.getName(),
							// new float [] {startptx,mY +
							// pixel.getWidth()/4,startptx + dx,mY +
							// pixel.getWidth()*5/4});

						} else {
							// mCanvas.drawBitmap(tip3 , startptx, startpty,
							// null);
							// mCanvas.drawBitmap(tip4, Math.max(allowedX, mX),
							// Math.max(allowedY, mY), null);
							// mPath.reset();
							// mPath.moveTo(startptx, startpty
							// +pixel.getHeight());
							// float endX = (Math.max(allowedX, mX) +
							// startptx)/2;
							// float endY= (Math.max(allowedY, mY) +
							// startpty)/2;
							// mPath.quadTo(startptx, startpty
							// +pixel.getHeight(), endX, endY);
							// mPath.quadTo(endX, endY,Math.max(allowedX,
							// mX),Math.max(allowedY, mY));
							// for(int j=1;j<ysquares-1;j++)
							// mCanvas.drawBitmap(pixel,mX,startpty
							// +(j*pixel.getHeight()),null);
							// mCanvas.drawPath(mPath, mPaint);
							mCanvas.drawBitmap(tip3, mX, startpty, null);
							mCanvas.drawBitmap(tip4, mX, startpty + dy
									- (pixel.getHeight()), null);
							for (int j = 1; j < ysquares - 1; j++)
								mCanvas.drawBitmap(pixel, mX, startpty
										+ (j * pixel.getHeight()), null);

							startNode.addEdge(currentNode, startNode.getName()
									+ "-" + currentNode.getName(), new float[] {
									mX, startpty, mX + pixel.getWidth(),
									startpty + dy });
							// currentNode.addEdge(startNode,
							// currentNode.getName()+"-"+startNode.getName(),
							// new float [] {mX,startpty,mX +
							// pixel.getWidth(),startpty + dy});

						}
						// mX = x;
						// mY = y;

						// if (!startNode.containsNode(currentNode))

						// if (!currentNode.containsNode(startNode))

						debug += " to Node " + currentNode.getName() + " ";
						// Edge currentEdge= Edge.getGraphEdge(startNode,
						// currentNode).get(0);
						// debug += currentEdge.getName();

					}
				}
			break;
		case 1:
			hasDrawn = false;
			hasMoved = false;
			break;
		}

		hasTouchStarted = false;
	}

	private boolean checkXYCoordinates(float x, float y) {
		boolean check = false;
		int posY = -1;
		int posX = -1;

		switch (currentComponent) {

		case 0:
			for (int i = 0; i < Ay.length; i++) {
				if (y >= (Ay[i] * dheight + offsety)
						&& y <= (By[i] * dheight + offsety)) {
					// debug+= "  Y " +
					// String.valueOf((Ycoordinate[i]-1)*hpixel) + " " +
					// String.valueOf((Ycoordinate[i])*hpixel);
					check = true;
					posY = i;
					allowedY = (Ay[i] * dheight + offsety);
					// debug+= "  Y" + String.valueOf(i);
					break;
				}
			}
			if (check) {
				check = false;

				if (posY == 0 || posY == 1)
					for (int i = 0; i < Ax1.length; i++) {
						if (x >= (Ax1[i] * dwidth + offsetx)
								&& x <= (Bx1[i] * dwidth + offsetx)) {
							// debug+= "\n  X " +
							// String.valueOf((Xcoordinate1[i]-1)*wpixel) + " "
							// + String.valueOf((Xcoordinate1[i])*wpixel);
							posX = i;
							check = true;
							allowedX = (Ax1[i] * dwidth + offsetx);
							// debug+= "  Y" + String.valueOf(i);
							currentNode = fixedNodesArray[posY];
							break;
						}
					}

				else if (posY == Ay.length - 2 || posY == Ay.length - 1)
					for (int i = 0; i < Ax4.length; i++) {
						if (x >= (Ax4[i] * dwidth + offsetx)
								&& x <= (Bx4[i] * dwidth + offsetx)) {
							// debug+= "\n X " +
							// String.valueOf((Xcoordinate2[i]-1)*wpixel) + " "
							// +String.valueOf((Xcoordinate2[i])*wpixel);
							check = true;
							posX = i;
							currentNode = fixedNodesArray[posY + 61
									- (Ay.length - 2) + 1];
							allowedX = (Ax4[i] * dwidth + offsetx);
							// debug+= "  X" + String.valueOf(i);
							break;
						}
					}
				else {

					for (int i = 0; i < Ax2.length; i++) {

						if (x >= (Ax2[i] * dwidth + offsetx)
								&& x <= (Bx2[i] * dwidth + offsetx)) {
							// debug+= "\n X " +
							// String.valueOf((Xcoordinate2[i]-1)*wpixel) + " "
							// +String.valueOf((Xcoordinate2[i])*wpixel);
							check = true;
							// debug+= "  X" + String.valueOf(i);
							allowedX = (Ax2[i] * dwidth + offsetx);
							posX = i;
							if (posY >= 2 && posY <= 6)
								currentNode = fixedNodesArray[posX + 2];
							else
								currentNode = fixedNodesArray[posX + 32];
							// break;
						}

						if ((posY == 6 || posY == 7)
								&& (posX == 12 || posX == 13 || posX == 14
										|| posX == 15 || posX == 16 || posX == 17)) {
							debug += "Forbidden position";
							check = false;

							// break;
						}

					}

				}
			}
			break;
		case 1:
			for (int i = 2; i < Ay.length - 2; i++) {
				if (y >= (Ay[i] * dheight + offsety)
						&& y <= (By[i] * dheight + offsety)) {
					// debug+= "  Y " +
					// String.valueOf((Ycoordinate[i]-1)*hpixel) + " " +
					// String.valueOf((Ycoordinate[i])*hpixel);
					check = true;
					posY = i;
					allowedY = (Ay[i] * dheight + offsety);
					// debug+= "  Y" + String.valueOf(i);
					// break;
				}
			}

			if (check) {
				for (int i = 0; i < Ax2.length; i++) {

					if (x >= (Ax2[i] * dwidth + offsetx)
							&& x <= (Bx2[i] * dwidth + offsetx)) {
						// debug+= "\n X " +
						// String.valueOf((Xcoordinate2[i]-1)*wpixel) + " "
						// +String.valueOf((Xcoordinate2[i])*wpixel);
						check = true;
						// debug+= "  X" + String.valueOf(i);
						allowedX = (Ax2[i] * dwidth + offsetx);
						posX = i;

						// if (posX==29||posX==28||posX==27)
						// {
						// posX=26;
						// allowedX = (Ax2[posX]*dwidth + offsetx);
						// }

						if (posY >= 2 && posY <= 6)
							currentNode = fixedNodesArray[posX + 2];
						else
							currentNode = fixedNodesArray[posX + 32];

						// break;
					}

					if (posY == 6 || posY == 7) {
						if (posX == 12 || posX == 13 || posX == 14
								|| posX == 15)// ||posX==16||posX==17)
						{
							// debug+= "Forbidden position";
							check = false;

							// break;
						}
						if (posX == 11 || posX == 10 || posX == 9) {
							check = true;
							posX = 8;
							allowedX = (Ax2[posX] * dwidth + offsetx);
						}

						if (posY >= 2 && posY <= 6)
							currentNode = fixedNodesArray[posX + 2];
						else
							currentNode = fixedNodesArray[posX + 32];
					}

				}

			}

			break;
		}
		return check;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		// debug= String.valueOf(x)+ " " + String.valueOf(y);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (bbMode == 0) {
				touch_start(x, y);
			} else if (bbMode == 1) {
			}
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			if (bbMode == 0)
				touch_move(x, y);
			else if (bbMode == 1) {
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:

			if (bbMode == 0) {
				touch_up(x, y);
				invalidate();
			} else if (bbMode == 1) {
				deleteProtocol(x, y);
				invalidate();
				// deleteBitmap = Bitmap.createBitmap((int)(1.2f*getWidth()),
				// getHeight(), Bitmap.Config.ARGB_8888);
				// mPath.reset();
			}

			break;
		}
		return true;
	}

	private void deleteProtocol(float x, float y) {
		
		Edge hilitedEdge = Edge.getEdgefromCoordinate(x, y);

		if (hilitedEdge != null)
		// {deleteCanvas.drawRect(hilitedEdge.getStartX(),
		// hilitedEdge.getStartY(), hilitedEdge.getEndX(),
		// hilitedEdge.getEndY(), mPaint);}
		{
			debug = hilitedEdge.getName();
			mPath.reset();
			mPath.moveTo(hilitedEdge.getStartX(), hilitedEdge.getStartY());
			mPath.addRect(hilitedEdge.getStartX(), hilitedEdge.getStartY(),
					hilitedEdge.getEndX(), hilitedEdge.getEndY(),
					Path.Direction.CW);
		} else {
			mCanvas.drawText("Null noni", 100, 105, mPaint);
		}

		currentEdge = hilitedEdge;
	}

	public void deleteEdge() {

		if (currentEdge != null) {
			mPath.reset();
			Node startNode = currentEdge.getStartNode(), endNode = currentEdge
					.getEndNode();

			if (currentEdge.getEdgeType() == 0) {

				startNode.removeNode(endNode);
				endNode.removeNode(startNode);
			}
			Edge.graphEdges.remove(currentEdge);
			// Edge.graphEdges.remove(currentEdge);
			mBitmap.eraseColor(0x00000000);resistorNo--;			
			for (Edge edge : Edge.graphEdges) {
				if (edge.getEdgeType() == 0)
					drawEdges(edge.getStartX(), edge.getStartY(), edge
							.getEndX(), edge.getEndY());
				else if (edge.getEdgeType() == 1) {
					drawResistorEdges(edge.getStartX(), edge.getStartY(), edge
							.getEndX(), edge.getEndY());
					
				}
				// debug2 += " " + edge.getName();
			}

			invalidate();
		}
		currentEdge=null;
	}

	public String verifyConnection() {
		String verificationResult = "";
		Verifier.resistorNodeArray = this.resistorNodes;
		ArrayList<Node> resistorNodes = new ArrayList<Node>(this.resistorNodes);

		switch (LabConfiguration) {
		// Unity
		case 0: {
			if (!Verifier.getConnection(fixedNodesArray[46],
					fixedNodesArray[63])) {
				verificationResult = "Error at Pin 3 (V+ to Ground)";
			} else if (!Verifier.getConnection(fixedNodesArray[0],
					fixedNodesArray[45])) {
				verificationResult = "Error at Pin 2 (V- to Vin)";
			} else if (!Verifier.getConnection(fixedNodesArray[0],
					fixedNodesArray[16])) {
				verificationResult = "Error between Pin 2 and Pin 6 (Feedback between V- and Vout)";
			} else if (!Verifier.getConnection(fixedNodesArray[16],
					fixedNodesArray[31])) {
				verificationResult = "Error at Pin 6 (Vout to output)";
			} else {
				verificationResult = "Connection Ok";
			}
		}
			break;

		// Inverting
		case 1: {
			debug2 = "";
			if (resistorNo == 2) {

				Node Ra1 = new Node("dummy"), Ra2 = new Node("dummy"), Rb1 = new Node(
						"dummy"), Rb2 = new Node("dummy");
				int sum1 = 0, sum2 = 0;
				int tempNodeValue;
				int i = 0, j = 0;
				int forbiddenIndex1 = 0, forbiddenIndex2 = 0;

				for (Node n : resistorNodes) {
					tempNodeValue = Verifier.getConnectionCount(
							fixedNodesArray[0], n);
					sum1 += tempNodeValue;

					if (tempNodeValue == 1) {
						Ra1 = n;
						forbiddenIndex1 = i;
						if (i % 2 == 1) {
							Ra2 = resistorNodes.get(i - 1);
							forbiddenIndex2 = i - 1;
						} else {
							Ra2 = resistorNodes.get(i + 1);
							forbiddenIndex2 = i + 1;
						}

					}
					i++;
					// tempNodeValue2 = Verifier.getConnectionCount(startNode,
					// endNode)

				}

				// i=0;
				for (Node n : resistorNodes) {
					if (j != forbiddenIndex1 && j != forbiddenIndex2) {
						tempNodeValue = Verifier.getConnectionCount(Ra2, n);
						sum2 += tempNodeValue;

						if (tempNodeValue == 1) {
							Rb1 = n;
							if (j % 2 == 1)
								Rb2 = resistorNodes.get(j - 1);
							else
								Rb2 = resistorNodes.get(j + 1);

						}

					}
					j++;
				}

				debug2 = " Ra1 " + Ra1.getName() + "  Ra2 " + Ra2.getName()
						+ " Rb1 " + Rb1.getName() + "  Rb2 " + Rb2.getName()
						+ " sum1  " + sum1 + " sum2  " + sum2 + " j  " + j;

				if (sum1 != 1 || sum2 != 1 || Ra1.equals(new Node("dummy"))
						|| Ra2.equals(new Node("dummy"))
						|| Rb1.equals(new Node("dummy"))
						|| Rb2.equals(new Node("dummy"))) {
					verificationResult = "Incorrect Connection";

				}

				else if (!Verifier.getConnection(fixedNodesArray[46],
						fixedNodesArray[63])) {
					verificationResult = "Error at Pin 3 (V+ to Ground)";
				}

				else if (!Verifier.getConnection(fixedNodesArray[0], Ra1)) {
					verificationResult = "Error at connection between Vin and Ri";
				} else if (!Verifier.getConnection(Ra2, fixedNodesArray[45])) {
					verificationResult = "Error at Pin 2 (V- to Resistor Ri) ";

				}

				else if (!Verifier.getConnection(Ra2, Rb1)) {
					verificationResult = "Error at connection between the Resistances (Rf and Ri) ";

				}

				else if (!Verifier.getConnection(Rb2, fixedNodesArray[16])) {
					verificationResult = "Error at Pin 6 (Rf to Vout)";
				} else if (!Verifier.getConnection(fixedNodesArray[16],
						fixedNodesArray[31])) {
					verificationResult = "Error at Pin 6 (Vout to output)";
				}

				else {
					Node[] problemNode = getBridgedNodes(new Node[] { Ra1, Ra2,
							Rb1, Rb2 });
					if (!problemNode[0].getName().equals("dummy")) {

						verificationResult = " Faulty Connection: "
								+ problemNode[0].getName() + " connected to "
								+ problemNode[1].getName();
					} else
						verificationResult = "Connection Ok";
				}
			}

			else {
				verificationResult = "Incomplete Connection";
			}
		}
			break;

		// Non-Inverting
		case 2: {
			debug2 = "";
			if (resistorNo == 2) {

				Node Ra1 = new Node("dummy"), Ra2 = new Node("dummy"), Rb1 = new Node(
						"dummy"), Rb2 = new Node("dummy");
				int sum1 = 0, sum2 = 0;
				int tempNodeValue;
				int i = 0, j = 0;
				int forbiddenIndex1 = 0, forbiddenIndex2 = 0;

				for (Node n : resistorNodes) {
					tempNodeValue = Verifier.getConnectionCount(
							fixedNodesArray[63], n);
					sum1 += tempNodeValue;

					if (tempNodeValue == 1) {
						Ra1 = n;
						forbiddenIndex1 = i;
						if (i % 2 == 1) {
							Ra2 = resistorNodes.get(i - 1);
							forbiddenIndex2 = i - 1;
						} else {
							Ra2 = resistorNodes.get(i + 1);
							forbiddenIndex2 = i + 1;
						}

					}
					i++;
					// tempNodeValue2 = Verifier.getConnectionCount(startNode,
					// endNode)

				}

				// i=0;
				for (Node n : resistorNodes) {
					if (j != forbiddenIndex1 && j != forbiddenIndex2) {
						tempNodeValue = Verifier.getConnectionCount(Ra2, n);
						sum2 += tempNodeValue;

						if (tempNodeValue == 1) {
							Rb1 = n;
							if (j % 2 == 1)
								Rb2 = resistorNodes.get(j - 1);
							else
								Rb2 = resistorNodes.get(j + 1);

						}

					}
					j++;
				}

				debug2 = " Ra1 " + Ra1.getName() + "  Ra2 " + Ra2.getName()
						+ " Rb1 " + Rb1.getName() + "  Rb2 " + Rb2.getName()
						+ " sum1  " + sum1 + " sum2  " + sum2 + " j  " + j;

				if (sum1 != 1 || sum2 != 1 || Ra1.equals(new Node("dummy"))
						|| Ra2.equals(new Node("dummy"))
						|| Rb1.equals(new Node("dummy"))
						|| Rb2.equals(new Node("dummy"))) {
					verificationResult = "Incorrect Connection";

				}

				else if (!Verifier.getConnection(fixedNodesArray[46],
						fixedNodesArray[0])) {
					verificationResult = "Error at Pin 3 (V+ to Vin)";
				}

				else if (!Verifier.getConnection(fixedNodesArray[63], Ra1)) {
					verificationResult = "Error at connection between Ground and Ri";
				} else if (!Verifier.getConnection(Ra2, fixedNodesArray[45])) {
					verificationResult = "Error at Pin 2 (V+ to Resistor Ri) ";

				}

				else if (!Verifier.getConnection(Ra2, Rb1)) {
					verificationResult = "Error at connection between the Resistances (Rf and Ri) ";

				}

				else if (!Verifier.getConnection(Rb2, fixedNodesArray[16])) {
					verificationResult = "Error at Pin 6 (Rf to Vout)";
				} else if (!Verifier.getConnection(fixedNodesArray[16],
						fixedNodesArray[31])) {
					verificationResult = "Error at Pin 6 (Vout to output)";
				}

				else {
					Node[] problemNode = getBridgedNodesNonInverting(new Node[] {
							Ra1, Ra2, Rb1, Rb2 });
					if (!problemNode[0].getName().equals("dummy")) {

						verificationResult = " Faulty Connection: "
								+ problemNode[0].getName() + " connected to "
								+ problemNode[1].getName();
					} else
						verificationResult = "Connection Ok";
				}
			}

			else {
				verificationResult = "Incomplete Connection";
			}
		}
			break;

		// difference
		case 3:

			debug2 = "";
			if (resistorNo == 4) {

				Node Ra1 = new Node("dummy"), Ra2 = new Node("dummy"), Rb1 = new Node(
						"dummy"), Rb2 = new Node("dummy");
				Node Rc1 = new Node("dummy"), Rc2 = new Node("dummy"), Rd1 = new Node(
						"dummy"), Rd2 = new Node("dummy");

				int sum1 = 0, sum2 = 0, sum3 = 0;
				int tempNodeValue;
				// int i=0,j=0;
				int forbiddenIndex1 = 0, forbiddenIndex2 = 0;

				Node RAtemp = new Node("dummy"), RCTemp = new Node("dummy");
				for (int i = 0; i < resistorNodes.size(); i++) {
					Node n = resistorNodes.get(i);
					tempNodeValue = Verifier.getConnectionCount(
							fixedNodesArray[0], n);
					sum1 += tempNodeValue;

					if (tempNodeValue == 1) {
						// Ra1 = n;
						if (i % 2 == 1) {
							if (Verifier.getConnection(
									resistorNodes.get(i - 1),
									fixedNodesArray[45])) {
								Ra1 = n;
								Ra2 = resistorNodes.get(i - 1);
							} else if (Verifier.getConnection(resistorNodes
									.get(i - 1), fixedNodesArray[46])) {
								Rc1 = n;
								Rc2 = resistorNodes.get(i - 1);
							}
							resistorNodes.remove(i);
							resistorNodes.remove(i - 1);
						} else {
							if (Verifier.getConnection(
									resistorNodes.get(i + 1),
									fixedNodesArray[45])) {
								Ra1 = n;
								Ra2 = resistorNodes.get(i + 1);
							} else if (Verifier.getConnection(resistorNodes
									.get(i + 1), fixedNodesArray[46])) {
								Rc1 = n;
								Rc2 = resistorNodes.get(i + 1);
							}
							resistorNodes.remove(i + 1);
							resistorNodes.remove(i);

						}
						// forbiddenIndex1 =i;

					}

					// tempNodeValue2 = Verifier.getConnectionCount(startNode,
					// endNode)

				}

				// i=0;
				for (int j = 0; j < resistorNodes.size(); j++) {

					Node n = resistorNodes.get(j);
					tempNodeValue = Verifier.getConnectionCount(Ra2, n);
					sum2 += tempNodeValue;

					if (tempNodeValue == 1) {
						Rb1 = n;
						if (j % 2 == 1) {
							resistorNodes.remove(j);
							Rb2 = resistorNodes.remove(j - 1);
						} else {
							Rb2 = resistorNodes.remove(j + 1);
							resistorNodes.remove(j);
						}

					}

				}

				int j1 = 0;

				for (int j = 0; j < resistorNodes.size(); j++) {

					Node n = resistorNodes.get(j);
					tempNodeValue = Verifier.getConnectionCount(Rc2, n);
					sum3 += tempNodeValue;

					if (tempNodeValue == 1) {
						Rd1 = n;
						if (j % 2 == 1) {
							resistorNodes.remove(j);
							Rd2 = resistorNodes.remove(j - 1);
						} else {
							Rd2 = resistorNodes.remove(j + 1);
							resistorNodes.remove(j);
						}

					}

					j1++;

				}

				debug2 = " Ra1 " + Ra1.getName() + "  Ra2 " + Ra2.getName()
						+ " Rb1 " + Rb1.getName() + "  Rb2 " + Rb2.getName()
						+ " Rc1 " + Rc1.getName() + "  Rc2 " + Rc2.getName()
						+ " Rd1 " + Rd1.getName() + "  Rd2 " + Rd2.getName()
						+ " sum1  " + sum1 + " sum2  " + sum2 + " sum3  "
						+ sum3 + " j  " + j1;

				if (sum1 != 2 || sum2 != 1 || sum3 != 1
						|| Ra1.equals(new Node("dummy"))
						|| Ra2.equals(new Node("dummy"))
						|| Rb1.equals(new Node("dummy"))
						|| Rb2.equals(new Node("dummy"))
						|| Rc1.equals(new Node("dummy"))
						|| Rc2.equals(new Node("dummy"))
						|| Rd1.equals(new Node("dummy"))
						|| Rd2.equals(new Node("dummy"))) {
					verificationResult = "Incorrect Connection";

				}

				else if (!Verifier.getConnection(fixedNodesArray[46], Rd1)) {
					verificationResult = "Error at Pin 3 (V+ to Grounding Resistance Rd)";
				}

				else if (!Verifier.getConnection(fixedNodesArray[46], Rc2)) {
					verificationResult = "Error at Pin 3 (V+ to Resistor Rc)";
				}

				else if (!Verifier.getConnection(Rd2, fixedNodesArray[63])) {
					verificationResult = "Error at connection between Rd and Ground)";
				}

				else if (!Verifier.getConnection(fixedNodesArray[0], Ra1)) {
					verificationResult = "Error at connection between Vin and Ra";
				}

				else if (!Verifier.getConnection(fixedNodesArray[0], Rc1)) {
					verificationResult = "Error at connection between Vin and Rc";
				} else if (!Verifier.getConnection(Ra2, fixedNodesArray[45])) {
					verificationResult = "Error at Pin 2 (V- to Resistor Ra) ";

				} else if (!Verifier.getConnection(Ra2, Rb1)) {
					verificationResult = "Error at connection between the Resistors Rb and Ra ";

				} else if (!Verifier.getConnection(Rb2, fixedNodesArray[16])) {
					verificationResult = "Error at Pin 6 (Rb to Vout)";
				} else if (!Verifier.getConnection(fixedNodesArray[16],
						fixedNodesArray[31])) {
					verificationResult = "Error at Pin 6 (Vout to output)";
				}

				else {
					Node[] problemNode = getBridgedNodesDifferenceAmp(new Node[] {
							Ra1, Ra2, Rb1, Rb2, Rc1, Rc2, Rd1, Rd2 });
					if (!problemNode[0].getName().equals("dummy")) {

						verificationResult = " Faulty Connection: "
								+ problemNode[0].getName() + " connected to "
								+ problemNode[1].getName();
					} else
						verificationResult = "Connection Ok";
				}
			}

			else {
				verificationResult = "Incomplete Connection";
			}

			break;
		}
		return verificationResult;
	}

	public void switchComponent() {

		currentComponent++;
		currentComponent %= 2;
	}

	public int flipMode() {
		mPath.reset();
		bbMode++;
		bbMode %= 2;
		if (bbMode == 0) {//
			mPaint.setColor(getResources().getColor(R.color.textViewColor));
		}

		else if (bbMode == 1) {
			mPaint.setColor(0xFF000000);

		}
		invalidate();
		return bbMode;
	}

	public void drawEdges(float x, float y, float x2, float y2) {
		// deleteCanvas.drawRect(, top, right, bottom, paint);
		float allowedX = x, allowedY = y, mX = x2, mY = y2;
		float dx = Math.abs(allowedX - mX);
		float dy = Math.abs(allowedY - mY);
		int xsquares = (int) (dx / pixel.getWidth());
		int ysquares = (int) (dy / pixel.getHeight());
		float startptx = Math.min(allowedX, mX) + pixel.getWidth() / 2;
		float startpty = Math.min(allowedY, mY) + pixel.getHeight() / 2;

		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {

			if (xsquares >= ysquares) {
				mCanvas.drawBitmap(tip, startptx, mY - pixel.getHeight(), null);
				mCanvas.drawBitmap(tip2, startptx + dx - (tip2.getWidth()), mY
						- pixel.getWidth(), null);
				for (int i = 1; i < xsquares - 1; i++)
					mCanvas.drawBitmap(pixel,
							startptx + (i * pixel.getWidth()), mY
									- pixel.getWidth(), null);
			} else {

				mCanvas
						.drawBitmap(tip3, mX - pixel.getHeight(), startpty,
								null);
				mCanvas.drawBitmap(tip4, mX - pixel.getHeight(), startpty + dy
						- (pixel.getHeight()), null);
				for (int j = 1; j < ysquares - 1; j++)
					mCanvas.drawBitmap(pixel, mX - pixel.getHeight(), startpty
							+ (j * pixel.getHeight()), null);

			}
		}

	}

	public void drawResistorEdges(float x, float y, float x2, float y2) {
		mCanvas.drawBitmap(resistor1, x, y, null);
	}

	private Node[] getBridgedNodes(Node[] arrangedResistorNodes) {
		Node[] forbidden = { arrangedResistorNodes[0],
				arrangedResistorNodes[1], arrangedResistorNodes[2],
				arrangedResistorNodes[3], fixedNodesArray[0],
				fixedNodesArray[2], fixedNodesArray[14], fixedNodesArray[15],
				fixedNodesArray[16], fixedNodesArray[17], fixedNodesArray[31],
				fixedNodesArray[32], fixedNodesArray[44], fixedNodesArray[45],
				fixedNodesArray[46], fixedNodesArray[47], fixedNodesArray[63] };

		Node[] problemNodes = { new Node("dummy"), new Node("dummy") };

		outer: for (int i = 0; i < forbidden.length; i++) {
			for (int j = i + 1; j < forbidden.length; j++) {
				if (i == 0 && j == 4)
					continue;
				else if ((i == 1 || i == 2) && (j == 2 || j == 13))
					continue;
				else if ((i == 3 || i == 8) && (j == 8 || j == 10))
					continue;
				else if (i == 14 && j == 16)
					continue;
				if (Verifier.getConnection(forbidden[i], forbidden[j])) {
					problemNodes[0] = forbidden[i];
					problemNodes[1] = forbidden[j];
					break outer;
				}
			}
		}
		return problemNodes;
	}

	private Node[] getBridgedNodesNonInverting(Node[] arrangedResistorNodes) {

		Node[] forbidden = { arrangedResistorNodes[0],
				arrangedResistorNodes[1], arrangedResistorNodes[2],
				arrangedResistorNodes[3], fixedNodesArray[0],
				fixedNodesArray[2], fixedNodesArray[14], fixedNodesArray[15],
				fixedNodesArray[16], fixedNodesArray[17], fixedNodesArray[31],
				fixedNodesArray[32], fixedNodesArray[44], fixedNodesArray[45],
				fixedNodesArray[46], fixedNodesArray[47], fixedNodesArray[63] };

		Node[] problemNodes = { new Node("dummy"), new Node("dummy") };

		outer: for (int i = 0; i < forbidden.length; i++) {
			for (int j = i + 1; j < forbidden.length; j++) {
				if (i == 0 && j == 16)
					continue;
				else if ((i == 1 || i == 2) && (j == 2 || j == 13))
					continue;
				else if ((i == 3 || i == 8) && (j == 8 || j == 10))
					continue;
				else if (i == 4 && j == 14)
					continue;
				if (Verifier.getConnection(forbidden[i], forbidden[j])) {
					problemNodes[0] = forbidden[i];
					problemNodes[1] = forbidden[j];
					break outer;
				}
			}
		}
		return problemNodes;
	}

	private Node[] getBridgedNodesDifferenceAmp(Node[] arrangedResistorNodes) {

		Node[] forbidden = { arrangedResistorNodes[0],
				arrangedResistorNodes[1], arrangedResistorNodes[2],
				arrangedResistorNodes[3], arrangedResistorNodes[4],
				arrangedResistorNodes[5], arrangedResistorNodes[6],
				arrangedResistorNodes[7], fixedNodesArray[0],
				fixedNodesArray[2], fixedNodesArray[14], fixedNodesArray[15],
				fixedNodesArray[16], fixedNodesArray[17], fixedNodesArray[31],
				fixedNodesArray[32], fixedNodesArray[44], fixedNodesArray[45],
				fixedNodesArray[46], fixedNodesArray[47], fixedNodesArray[63] };

		Node[] problemNodes = { new Node("dummy"), new Node("dummy") };

		outer: for (int i = 0; i < forbidden.length; i++) {
			for (int j = i + 1; j < forbidden.length; j++) {
				if ((i == 0 || i == 4) && (j == 4 || j == 8))
					continue;
				else if ((i == 1 || i == 2) && (j == 2 || j == 17))
					continue;
				else if ((i == 3 || i == 12) && (j == 12 || j == 14))
					continue;
				else if ((i == 5 || i == 6) && (j == 6 || j == 18))
					continue;
				else if (i == 7 && j == 20)
					continue;
				if (Verifier.getConnection(forbidden[i], forbidden[j])) {
					problemNodes[0] = forbidden[i];
					problemNodes[1] = forbidden[j];
					break outer;
				}
			}
		}
		return problemNodes;

	}

	private void getResistorCoordinates(float x3, float y3) {

		if (moveRight) {
			boolean isTip = false;

			if (x3 >= Ax2[29] * dwidth + offsetx
					&& x3 <= Bx2[29] * dwidth + offsetx) {
				isTip = true;
			} else if (x3 >= Ax2[28] * dwidth + offsetx
					&& x3 <= Bx2[28] * dwidth + offsetx) {
				isTip = true;
			} else if (x3 >= Ax2[27] * dwidth + offsetx
					&& x3 <= Bx2[27] * dwidth + offsetx) {
				isTip = true;
			}
			if (isTip) {

				mX = (Ax2[26] * dwidth + offsetx);

				if (y3 >= Ay[2] * dheight + offsety
						&& y3 <= Ay[6] * dheight + offsety)
					startNode = fixedNodesArray[28];
				else
					startNode = fixedNodesArray[40];
			} else {
				// same effect as using posY ==6 || posY==7
				if (y3 >= Ay[6] * dheight + offsety
						&& y3 <= By[7] * dheight + offsety) {

					boolean isMid = false;

					if (x3 >= Ax2[11] * dwidth + offsetx
							&& x3 <= Bx2[11] * dwidth + offsetx) {
						isMid = true;
					} else if (x3 >= Ax2[10] * dwidth + offsetx
							&& x3 <= Bx2[10] * dwidth + offsetx) {
						isMid = true;
					} else if (x3 >= Ax2[9] * dwidth + offsetx
							&& x3 <= Bx2[9] * dwidth + offsetx) {
						isMid = true;
					}
					if (isMid) {
						mX = (Ax2[8] * dwidth + offsetx);

						if (y3 >= Ay[2] * dheight + offsety
								&& y3 <= Ay[6] * dheight + offsety)
							startNode = fixedNodesArray[10];
						else
							startNode = fixedNodesArray[40];
					}
				}
			}
		} else {
			
			boolean isTip = false;

			if (x3 >= Ax2[0] * dwidth + offsetx
					&& x3 <= Bx2[0] * dwidth + offsetx) {
				isTip = true;
			} else if (x3 >= Ax2[1] * dwidth + offsetx
					&& x3 <= Bx2[1] * dwidth + offsetx) {
				isTip = true;
			} else if (x3 >= Ax2[2] * dwidth + offsetx
					&& x3 <= Bx2[2] * dwidth + offsetx) {
				isTip = true;
			}
			if (isTip) {

				mX = (Ax2[0] * dwidth + offsetx);

				if (y3 >= Ay[2] * dheight + offsety
						&& y3 <= Ay[6] * dheight + offsety)
					startNode = fixedNodesArray[2];
				else
					startNode = fixedNodesArray[32];
			} else {
				
				mX=x3-resistor1.getWidth()+pixel.getWidth();
				mY=y3-resistor1.getWidth()/4;
				
				currentNode= fixedNodesArray[Integer
												.parseInt(startNode.getName()
														.substring(1)) - 3];
				// same effect as using posY ==6 || posY==7
				if (y3 >= Ay[6] * dheight + offsety
						&& y3 <= By[7] * dheight + offsety) {

					boolean isMid = false;

					if (x3 >= Ax2[16] * dwidth + offsetx
							&& x3 <= Bx2[16] * dwidth + offsetx) {
						isMid = true;
					} else if (x3 >= Ax2[17] * dwidth + offsetx
							&& x3 <= Bx2[17] * dwidth + offsetx) {
						isMid = true;
					} else if (x3 >= Ax2[18] * dwidth + offsetx
							&& x3 <= Bx2[18] * dwidth + offsetx) {
						isMid = true;
					}
					if (isMid) {
						mX = (Ax2[16] * dwidth + offsetx);

						if (y3 >= Ay[2] * dheight + offsety
								&& y3 <= Ay[6] * dheight + offsety)
							startNode = fixedNodesArray[18];
						else
							startNode = fixedNodesArray[48];
					}
				}
			}

		}

	}

}
