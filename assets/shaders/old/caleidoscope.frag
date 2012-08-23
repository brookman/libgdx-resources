uniform sampler2D tex;
uniform float time;

void main( void )
{
	float lines = 8.0;

	vec2 coo = gl_TexCoord[0].st;
	float linesSeg = 1.0 / lines;

	//deal with y coordinates and flipping
	float modulo = mod(coo.y, linesSeg);
	float modulo2 = mod(coo.y, linesSeg*2.0);
	coo.y = modulo;
	if(modulo2 > linesSeg){
		coo.y = linesSeg - coo.y;	
	}

	//coo.x+=sin(time/20.0);
	//coo.y+=sin(time/16.0);

	gl_FragColor = texture2D(tex, coo);
}
