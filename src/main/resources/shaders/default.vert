#version 330

in vec2 POSITION; // position of the vertex in object space
in vec2 TEXCOORD; // texture coordinates of the vertex

uniform mat4 MVP; // model-view-projection matrix
uniform vec2 TRANSLATION; // translation of the vertex in model space
uniform vec2 SCALE; // scaling of the vertex in model space
uniform vec3 COLOR;

out vec3 color;
out vec2 texcoord;

void main(void)
{
	gl_Position = MVP * vec4((POSITION + TRANSLATION) * SCALE, 1.0, 1.0);
	
	color = COLOR;
	texcoord = TEXCOORD;
}