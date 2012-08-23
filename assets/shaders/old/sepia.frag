/**
 * @filter         Sepia
 * @description    Gives the image a reddish-brown monochrome tint that imitates an old photograph.
 * @param amount   0 to 1 (0 for no effect, 1 for full sepia coloring)
 */

uniform sampler2D textureImage;
uniform float amount;

void main( void )
{
	vec4 color = texture2D(textureImage, gl_TexCoord[0].st);
    float r = color.r;
    float g = color.g;
    float b = color.b;
	color.r = min(1.0, (r * (1.0 - (0.607 * amount))) + (g * (0.769 * amount)) + (b * (0.189 * amount)));
    color.g = min(1.0, (r * 0.349 * amount) + (g * (1.0 - (0.314 * amount))) + (b * 0.168 * amount));
    color.b = min(1.0, (r * 0.272 * amount) + (g * 0.534 * amount) + (b * (1.0 - (0.869 * amount))));
	gl_FragColor = color;
}