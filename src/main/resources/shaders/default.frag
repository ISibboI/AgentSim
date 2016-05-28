#version 330

precision mediump float;

in vec3 color;     // interpolated color passed from vertex shader

out vec4 frag_color; // the final color of the fragment

void main (void)
{
	frag_color = vec4(color, 1.0);
}