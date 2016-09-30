#version 330

precision mediump float;

in vec3 color;     // interpolated color passed from vertex shader
in vec2 texcoord;  // texture coordinates from vertex shader

out vec4 frag_color; // the final color of the fragment

uniform sampler2D MAP_TEX; // Texture of the map

void main (void)
{
	frag_color = vec4(color, 1.0) * texture(MAP_TEX, texcoord);
	frag_color = vec4(texcoord, 1.0, 1.0);
}