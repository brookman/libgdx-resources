uniform sampler2D tex;
uniform float time;

uniform vec2 shift;
uniform float segments;
uniform float rotSpeed;

void main( void )
{
	float n = 2.0 * asin(1.0) / segments;

	vec2 coo = gl_TexCoord[0].st;
	
	coo -= vec2(0.5, 0.5);

	float ang = atan(coo.y, coo.x);
	float len = sqrt( coo.x * coo.x + coo.y * coo.y);

	float count = ang / n;

	ang = mod(ang, n);

	if(mod(count, 2.0) < 1){
		ang = n - ang;
	}

	coo.x = cos(ang + time * 0.1) * len;
	coo.y = sin(ang + time * 0.1) * len;

	coo += vec2(0.5, 0.5);
	coo += vec2(sin(time*0.1), cos(time*0.0244));
	
	coo *= 4.0;

	gl_FragColor = texture2D(tex, coo);
}
