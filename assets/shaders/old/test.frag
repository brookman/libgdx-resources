#extension GL_EXT_gpu_shader4 : require 


unsigned int seed;
uniform float time;
uniform sampler2D tex0;

/* return pseudorandom float with values between 0 and 1. */
float random() {
	seed = (seed * 1103515245u + 12345u);
	return float(seed) / 4294967296.0;
} 

/* return pseudorandom vec3 with values between -1 and 1. */
vec3 random3() {	
	vec3 result;	
	seed = (seed * 1103515245u + 12345u);
	result.x = float(seed);
	seed = (seed * 1103515245u + 12345u);
	result.y = float(seed);
	seed = (seed * 1103515245u + 12345u);
	result.z = float(seed);
	return (result / 2147483648.0) - vec3(1,1,1);
}

void main(){
	vec2 coo = gl_TexCoord[0].xy;
	gl_FragColor = texture2D(tex0, coo);
	float s1 = 0.005;
	float s2 = 0.002;
	seed = uint(time*1000000);

	float c = gl_FragColor.r*0.333+gl_FragColor.g*0.333+gl_FragColor.b*0.333;
	gl_FragColor = vec4(c,c,c,1.0);

	for(int i=0;i<50;i++){
		if(distance(coo,random3().xy)<s1){
			gl_FragColor *= random()+0.5;
		}
	}

	for(int i=0;i<5;i++){
		float r = random3();
		if(coo.x>r&&coo.x<r+s2){
			gl_FragColor *= random()+0.7;
		}
	}


}