uniform float time;
uniform float zoom;
uniform float speed;
uniform sampler2D tex;

void main()	{
	vec2 c = (gl_TexCoord[0].st-vec2(0.5,0.5))*zoom;
	float t = (time-speed)*speed;
	float si = sin(t);
	float co = cos(t);
	gl_FragColor = texture2D(tex,vec2(c.x*co-c.y*si,c.y*co+c.x*si)+vec2(0.5,0.5));
}