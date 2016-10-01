#version 330

in vec3 POSITION; // position of the vertex in object space
in vec2 TEXCOORD; // texture coordinates of the vertex

uniform mat4 MVP; // model-view-projection matrix
uniform vec3 TRANSLATION; // translation of the vertex in model space
uniform vec3 SCALE; // scaling of the vertex in model space
uniform vec3 COLOR;

out vec3 color;
out vec2 texcoord;

void main(void)
{
	gl_Position = MVP * vec4((POSITION + TRANSLATION) * SCALE, 1.0);
	
	color = COLOR;
	texcoord = TEXCOORD;
}