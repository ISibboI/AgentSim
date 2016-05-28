#version 330

in vec2 POSITION; // position of the vertex in object space

uniform mat4 MVP; // model-view-projection matrix
uniform vec2 TRANSLATION; // translation applied to the position
uniform vec3 COLOR;

out vec3 color;

void main(void)
{
	gl_Position = MVP * vec4(POSITION + TRANSLATION, 1.0, 1.0);
	color = COLOR;
}