#extension GL_EXT_gpu_shader4 : require 

uniform float time;
uniform sampler2D tex0;

unsigned int seed;

/* return pseudorandom float with values between 0 and 1. */
float random() {
	seed = (seed * 1103515245u + 12345u);
	return float(seed) / 4294967296.0;
} 

/* return pseudorandom vec3 with values between -1 and 1. */
vec2 random2() {	
	vec2 result;	
	seed = (seed * 1103515245u + 12345u);
	result.x = float(seed);
	seed = (seed * 1103515245u + 12345u);
	result.y = float(seed);
	//seed = (seed * 1103515245u + 12345u);
	//result.z = float(seed);
	return (result / 2147483648.0) - vec2(1,1);
}

void main(){
	vec2 tex = gl_Textexrd[0].xy;
	vec4 col = texture2D(tex0, tex);
	seed = uint(fract(time*0.00000001)*2000000000);

	float s0 = 0.015*random();
	float s1 = 0.007*random();
	float s2 = 0.003*random();
	
	
	float c = col.r*0.3+col.g*0.59+col.b*0.11;

	for(int i=0;i<3;i++){
		if(distance(tex,random2())<s0){
			c *= random()+0.3;
		}
	}

	for(int i=0;i<20;i++){
		if(distance(tex,random2())<s1){
			c *= random()+0.3;
		}
	}

	for(int i=0;i<3;i++){
		float r = random();
		if(tex.x>r&&tex.x<r+s2){
			c *= random()+0.7;
		}
	}

	c-=random()*0.05;

	gl_FragColor = vec4(c,c,c,1.0);
}