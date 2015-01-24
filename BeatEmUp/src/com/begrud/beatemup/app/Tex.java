package com.begrud.beatemup.app;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import android.graphics.PointF;

public class Tex {
	/* these could possibly be in their own class called MESH */
	private static FloatBuffer vertexBuffer;	// buffer holding the vertices
	private static int vertexCount;			// number of vertices in the vertexBuffer
	private static FloatBuffer textureBuffer;	// buffer holding the texture coordinates

	/* this would be the constructor of the MESH obj */
	private void mkMesh()
	{
		/* this initialises the the two FloatBuffer objects */
	
		float vertices[] = {
			-0.5f, -0.5f,  0.0f,		// V1 - bottom left
			-0.5f,  0.5f,  0.0f,		// V2 - top left
			 0.5f, -0.5f,  0.0f,		// V3 - bottom right
			 0.5f,  0.5f,  0.0f			// V4 - top right
			};
		float texture[] = {    		
			// Mapping coordinates for the vertices
			0.0f, 1.0f,		// top left		(V2)
			0.0f, 0.0f,		// bottom left	(V1)
			1.0f, 1.0f,		// top right	(V4)
			1.0f, 0.0f		// bottom right	(V3)
			};
		/* cache the number of floats in the vertices data */
		vertexCount = vertices.length;
		
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		
		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);
		
		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	/* the draw methods use an EXTERNAL gl context, 
	 * since these methods do not rely on or depend on
	 * the specifics of the GL10 environment (namely texture IDs).
	 */
	private void drawMesh(GL10 gl)
	{
		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		// Draw the vertices as triangle strip
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertexCount / 3);

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	private void drawMesh(GL10 gl, float x, float y)
	{
		gl.glTranslatef(x,y,0.0f);
			this.drawMesh(gl);
		gl.glTranslatef(-x,-y,0.0f);
	}
	private void drawMesh(GL10 gl, float x, float y, float hor, float ver)
	{
		gl.glTranslatef(x,y,0.0f);
			gl.glScalef(hor, ver, 1);
				this.drawMesh(gl);
			gl.glScalef(1/hor, 1/ver, 1);
		gl.glTranslatef(-x,-y,0.0f);
	}
	/* NB.  these draw methods would be public iff MESH was its own class */
	
	
	/* 
	 *    the Texture object is a triple of the GL context, the TextureID and the Mesh 
	 * 
	 * we include the GL context because the TextureID only makes sense within a specific 
	 * GL Context.
	 * 
	 * we include a singleton instance of the Mesh because we have no need for more then one: we
	 * are only drawing rectangular sprites.
	 * 
	 * we include the TextureID because that is what this object is wrapping.
	 * 
	 * */
	
	private GL10 gl;
	private int texture_id;
	/* private static Mesh mesh */
	private boolean first_time = true;
	
	private int newTexture(Bitmap bmp)
	{
		int[] tex_id = new int[1];
		
		// generate one texture pointer
		gl.glGenTextures(1, tex_id, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[0]);

		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap 
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		
		return tex_id[0];
	}
	private int newTexture(Context context, int resource_id)
	{
		int tex_id;
		
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource_id);
			tex_id = newTexture(bmp);
		bmp.recycle();
		
		return tex_id;
	}
	/* should we bother with methods for overwriting the current texture? */
	
	public Tex(GL10 gl, Context context, int resource_id)
	{
		/* singleton instance of Mesh */
		if(first_time == true)
		{
			mkMesh();
			first_time = false;
		}

		this.gl = gl;
		this.texture_id = newTexture(context, resource_id);
	}
	/* 
	 * TODO what do we do about destructors? 
	 * 
	 * i imagine we have to free the textures.
	 * 
	 * */
	
	public void draw()
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture_id);
		drawMesh(gl);
	}
	public void draw(float x, float y)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture_id);
		drawMesh(gl, x, y);
	}
	public void draw(float x, float y, float hor, float ver)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture_id);
		drawMesh(gl,x,y,hor,ver);
	}
	public void draw(PointF p)
	{
		draw(p.x,p.y);
	}
	public void draw(PointF p, float scale)
	{
		draw(p.x,p.y,scale,scale);
	}
	/* these draw() methods draw the texture mapped mesh */
}
