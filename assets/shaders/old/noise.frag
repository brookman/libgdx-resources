uniform sampler2D textureImage;
uniform float amount;


float rand(vec2 co) {
	return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main( void )
{
	vec4 color = texture2D(textureImage, gl_TexCoord[0].st);
    float diff = (rand(gl_TexCoord[0].st) - 0.5) * amount;
    color.r += diff;
    color.g += diff;
    color.b += diff;
	gl_FragColor = color;
}