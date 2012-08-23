uniform sampler2D tex;
uniform float time;

void main( void )
{

	float s = fract(time);
	vec2 coo = gl_TexCoord[0].st;
	vec2 coo2 = gl_TexCoord[0].st;

	coo2.x += sin((coo.y-s)*5)/((coo.y-s)*(coo.y-s)*50)*0.02*(fract(time*100)-0.5);

	vec4 original = texture2D(tex, coo);
	vec4 shifted = texture2D(tex, coo2);
	vec4 color = vec4( shifted.r, shifted.g,  shifted.b, original.a);

	if(coo.y>s-0.01 && coo.y<s+0.01){
		color.b+=sin(time*22.2)*0.4;
	}

	float c = color.r*0.3333 + color.g*0.3333 + color.b * 0.3333;
	gl_FragColor = color;
}
