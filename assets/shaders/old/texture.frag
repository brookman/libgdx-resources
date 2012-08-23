/* texture.frag - texture lookup example */

//
// This defines a sampler.
// To use textures, load an image into a texture unit and
// assign the texture unit index to the sampler.
//
uniform sampler2D textureImage;

uniform float time;


//
// entry point
//
void main( void )
{
	gl_FragColor = texture2D( textureImage, gl_TexCoord[0].st );

	
	gl_FragColor.r = 0.3 + length(gl_TexCoord[0].st - vec2(0.5, 0.5)) * (1.0-frac(time*1)) * 0.9;
	gl_FragColor.a = 1.0;

	
}
