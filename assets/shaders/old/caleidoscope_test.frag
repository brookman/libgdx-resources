uniform sampler2D tex;
uniform float time;


void main( void )
{
	float lines = 8.0;
	float h = 0.86602540378443864676372317075294;
	float s = 1.7320508075688772935274463415059;

	vec2 coo = gl_TexCoord[0].st;
	float linesSeg = 1.0 / lines;




	//float t = (time-speed)*speed;
	//float si = sin(t);
	//float co = cos(t);
	//coo = vec2(coo.x*co-coo.y*si,coo.y*co+coo.x*si);//-vec2(0.5,0.5);

	//coo.x = abs(mod(coo.x + linesSeg, linesSeg * 2) - linesSeg);

	
	float sca = linesSeg*h;

	coo.x = mod(coo.x, linesSeg*3.0);
	coo.y = abs(mod(coo.y + sca, sca * 2.0) - sca);

	if(coo.x*s + coo.y > sca && coo.x*s - coo.y < sca){
		//NOP
	}
	if(coo.x*s - coo.y > sca && coo.x*s + coo.y < sca*3.0) {
		float diff = (coo.x*s - coo.y)-sca;
		coo.x -=1-diff;
		//coo.y -=0.01;
		//coo.y += diff;
		//coo.x = 0.0;
		gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	}
	if(coo.x*s + coo.y > sca*3.0 && coo.x*s - coo.y < sca*3.0){
		//gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);
	}
	if(coo.x*s - coo.y > sca*3.0 && coo.x*s + coo.y < sca*5.0){
		//gl_FragColor = vec4(1.0, 0.0, 1.0, 1.0);
	}
	if(coo.x*s + coo.y > sca*5.0 && coo.x*s - coo.y < sca*5.0){
		//gl_FragColor = vec4(0.0, 1.0, 1.0, 1.0);
	}
	if(coo.x*s + coo.y < sca || (coo.x*s - coo.y > sca*5.0 && coo.x*s + coo.y < sca*7.0)){
		//gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
	}

	gl_FragColor = texture2D(tex, coo);
}
