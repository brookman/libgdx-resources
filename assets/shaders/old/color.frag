uniform sampler2D textureImage;
uniform vec3 color;

void main(void)
{
	vec4 original_color = texture2D(textureImage,  gl_TexCoord[0].st);				
	float gray = dot(original_color.rgb, vec3(0.299, 0.587, 0.114));
	gl_FragColor = vec4(gray * color, original_color.a);
}