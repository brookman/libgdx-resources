uniform sampler2D tex;
uniform float bloom;
uniform float blur;

void main()
{
	vec4 sum = vec4(0);
	vec2 coo = gl_TexCoord[0].xy;
	int j;
	int i;

	for( i= -4 ;i < 4; i++) {
		for (j = -4; j < 4; j++) {
			sum += texture2D(tex, coo + vec2(j, i) * blur * 0.01) * bloom * 0.3;
		}
	}

	float c = (texture2D(tex, coo).r + texture2D(tex, coo).g + texture2D(tex, coo).b) / 3.0;
	float f = 0.0075;

	if (c < 0.5) {
		f = 0.009;
	}
	if (c < 0.3) {
		f = 0.012;
	}

	gl_FragColor = sum * sum * f + texture2D(tex, coo);
	
}