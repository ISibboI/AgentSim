package de.isibboi.agentsim.ui.opengl;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.ui.renderer.Renderer;

/**
 * The JPanel that draws the game content using OpenGL.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class GameGLJPanel implements GLEventListener {
	private static final Logger LOG = LogManager.getLogger(GameGLJPanel.class);

	private final GLJPanel _panel;
	private final HashSet<Drawable> _content = new HashSet<>();
	private Renderer _renderer;

	private float _aspectRatio;
	private float _scale = 1;
	private float[] _mvpMatrix;

	// Clear values
	private FloatBuffer _clearColor = GLBuffers.newDirectFloatBuffer(new float[] { 0, 0, 0, 1 });
	private FloatBuffer _clearDepth = GLBuffers.newDirectFloatBuffer(new float[] { 1 });

	// Buffers
	private static class Buffer {
		public static final int VERTEX = 0;
		public static final int ELEMENT = 1;
		public static final int TRANSFORM = 2;
		public static final int MAX = 3;
	}

	private IntBuffer _bufferName = GLBuffers.newDirectIntBuffer(Buffer.MAX);

	// Behind every 2D point are the texture coordinates!
	private float[] _vertexData = new float[] {
			0, 0, 0, 0,
			0, 1, 0, 1,
			1, 1, 1, 1,
			1, 0, 1, 0
	};
	private int _vertexCount = _vertexData.length / 4;
	private int _vertexSize = _vertexData.length * Float.BYTES;

	private short[] _elementData = new short[] {
			0, 3, 2, 0, 1
	};
	private int _elementCount = _elementData.length;
	private int _elementSize = _elementCount * Short.BYTES;

	// Vertex arrays
	private IntBuffer _vertexArrayName = GLBuffers.newDirectIntBuffer(1);

	// Shaders
	private static final String SHADERS_ROOT = "shaders";
	private int _programName;
	private int _mvpMatrixUL;
	private int _colorUL;
	private int _translationUL;
	private int _scalingUL;

	// Textures
	private Texture _whiteTexture;

	public GameGLJPanel() {
		GLProfile glProfile = GLProfile.get(GLProfile.GL3);
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		_panel = new GLJPanel(glCapabilities);

		// We want to draw stuff manually here!
		_panel.setIgnoreRepaint(true);

		_panel.addGLEventListener(this);

		// Recommended for performance reasons.
		// We can use the projection matrix for that if necessary.
		_panel.setSkipGLOrientationVerticalFlip(true);
	}

	public GLJPanel getJPanel() {
		return _panel;
	}

	@Override
	public void init(final GLAutoDrawable drawable) {
		LOG.info("Initializing GLJPanel");

		initBuffers(drawable.getGL().getGL3());
		initVertexArray(drawable.getGL().getGL3());
		initShaders(drawable.getGL().getGL3());
		initTextures(drawable.getGL().getGL3());

		LOG.info("Initialized GLJPanel");
	}

	private void initBuffers(final GL3 gl) {
		FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(_vertexData);
		ShortBuffer elementBuffer = GLBuffers.newDirectShortBuffer(_elementData);

		gl.glGenBuffers(Buffer.MAX, _bufferName);

		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _bufferName.get(Buffer.VERTEX));
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, _vertexSize, vertexBuffer, GL3.GL_STATIC_DRAW);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, _bufferName.get(Buffer.ELEMENT));
		gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, _elementSize, elementBuffer, GL3.GL_STATIC_DRAW);
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);

		checkError(gl, "initBuffers");
	}

	private void initVertexArray(final GL3 gl) {
		gl.glGenVertexArrays(1, _vertexArrayName);
		gl.glBindVertexArray(_vertexArrayName.get(0));

		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _bufferName.get(Buffer.VERTEX));

		int stride = 4 * Float.BYTES;

		gl.glEnableVertexAttribArray(Semantic.Attr.POSITION);
		gl.glVertexAttribPointer(Semantic.Attr.POSITION, 2, GL3.GL_FLOAT, false, stride, 0);
		gl.glEnableVertexAttribArray(Semantic.Attr.TEXCOORD);
		gl.glVertexAttribPointer(Semantic.Attr.TEXCOORD, 2, GL3.GL_FLOAT, false, stride, 2 * Float.BYTES);

		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, _bufferName.get(Buffer.ELEMENT));

		gl.glBindVertexArray(0);

		checkError(gl, "initVertexArray");
	}

	private void initShaders(final GL3 gl) {
		ShaderCode vertexShader = ShaderCode.create(gl, GL3.GL_VERTEX_SHADER, getClass(), SHADERS_ROOT, null, "default", "vert", null, true);
		ShaderCode fragmentShader = ShaderCode.create(gl, GL3.GL_FRAGMENT_SHADER, getClass(), SHADERS_ROOT, null, "default", "frag", null, true);
		ShaderProgram shaderProgram = new ShaderProgram();
		shaderProgram.add(vertexShader);
		shaderProgram.add(fragmentShader);

		shaderProgram.init(gl);
		shaderProgram.link(gl, System.out);
		_programName = shaderProgram.program();

		gl.glBindAttribLocation(_programName, Semantic.Attr.POSITION, "POSITION");
		gl.glBindAttribLocation(_programName, Semantic.Attr.TEXCOORD, "TEXCOORD");
		gl.glBindFragDataLocation(_programName, Semantic.Frag.COLOR, "frag_color");

		shaderProgram.link(gl, System.out);

		_mvpMatrixUL = gl.glGetUniformLocation(_programName, "MVP");
		_colorUL = gl.glGetUniformLocation(_programName, "COLOR");
		_translationUL = gl.glGetUniformLocation(_programName, "TRANSLATION");
		_scalingUL = gl.glGetUniformLocation(_programName, "SCALE");

		vertexShader.destroy(gl);
		fragmentShader.destroy(gl);

		checkError(gl, "initShaders");
	}

	private void initTextures(final GL3 gl) {
		BufferedImage white = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		white.setRGB(0, 0, 0xffffffff);
		_whiteTexture = AWTTextureIO.newTexture(gl.getGLProfile(), white, false);

		checkError(gl, "initTextures");
	}

	@Override
	public void dispose(final GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();

		gl.glDeleteProgram(_programName);
		gl.glDeleteVertexArrays(1, _vertexArrayName);
		gl.glDeleteBuffers(Buffer.MAX, _bufferName);
	}

	@Override
	public void display(final GLAutoDrawable drawable) {
		if (_renderer == null) {
			LOG.warn("Cannot render " + this + ", since _renderer is null");
			return;
		}

		LOG.trace("Displaying GLJPanel");

		// Configure OpenGL
		GL3 gl = drawable.getGL().getGL3();

		gl.glClearBufferfv(GL3.GL_COLOR, 0, _clearColor);
		gl.glClearBufferfv(GL3.GL_DEPTH, 0, _clearDepth);

		gl.glUseProgram(_programName);

		gl.glBindVertexArray(_vertexArrayName.get(0));

		gl.glUniformMatrix4fv(_mvpMatrixUL, 1, false, _mvpMatrix, 0);

		_renderer.setColorUL(_colorUL);
		_renderer.setTranslationUL(_translationUL);
		_renderer.setScalingUL(_scalingUL);
		_renderer.setElementSize(_elementSize);
		_renderer.setWhiteTexture(_whiteTexture);
		_renderer.startRender(gl);

		for (Drawable d : _content) {
			if (d == null) {
				LOG.error("Drawables container contains null element!");
			}

			d.accept(_renderer);
		}

		checkError(gl, "display");

		LOG.trace("Displayed GLJPanel");
	}

	@Override
	public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
		LOG.info("Reshaped GLJPanel: x = " + x + ", y = " + y + ", width = " + width + ", height = " + height);

		GL3 gl = drawable.getGL().getGL3();
		gl.glViewport(x, y, width, height);
		_aspectRatio = (float) width / height;
		final int scaleConstant = 6;
		_scale = (float) scaleConstant / height;

		_mvpMatrix = new float[16];
		float[] scaleMatrix = new float[16];
		FloatUtil.makeTranslation(_mvpMatrix, true, -width / scaleConstant, -height / scaleConstant, 0);
		FloatUtil.makeScale(scaleMatrix, true, _scale / _aspectRatio, _scale, _scale);
		_mvpMatrix = FloatUtil.multMatrix(scaleMatrix, _mvpMatrix);

		System.out.println(FloatUtil.matrixToString(null, "", "%10.5f", _mvpMatrix, 0, 4, 4, false));
	}

	private void checkError(GL3 gl, String location) {

		int error = gl.glGetError();

		if (error != GL3.GL_NO_ERROR) {
			String errorString;

			switch (error) {
			case GL3.GL_INVALID_ENUM:
				errorString = "GL_INVALID_ENUM";
				break;
			case GL3.GL_INVALID_VALUE:
				errorString = "GL_INVALID_VALUE";
				break;
			case GL3.GL_INVALID_OPERATION:
				errorString = "GL_INVALID_OPERATION";
				break;
			case GL3.GL_INVALID_FRAMEBUFFER_OPERATION:
				errorString = "GL_INVALID_FRAMEBUFFER_OPERATION";
				break;
			case GL3.GL_OUT_OF_MEMORY:
				errorString = "GL_OUT_OF_MEMORY";
				break;
			default:
				errorString = "UNKNOWN";
				break;
			}

			LOG.warn("OpenGL Error(" + errorString + "): " + location);
		}
	}

	/**
	 * Returns the drawables container.
	 * Use this to add or remove drawables. 
	 * @return The drawables container.
	 */
	public Collection<Drawable> getDrawablesContainer() {
		return _content;
	}

	/**
	 * Sets the renderer to use for rendering.
	 * @param renderer The renderer.
	 */
	public void setRenderer(final Renderer renderer) {
		_renderer = renderer;
	}
}