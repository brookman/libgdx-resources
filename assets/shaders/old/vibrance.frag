uniform sampler2D textureImage;
uniform float amount;

void main( void )
{
	vec4 color = texture2D(textureImage, gl_TexCoord[0].st);
 	float average = (color.r + color.g + color.b) / 3.0;
    float mx = max(color.r, max(color.g, color.b));
    float amt = (mx - average) * (-amount * 3.0);
    color.rgb = mix(color.rgb, vec3(mx), amt);
    gl_FragColor = color;
}