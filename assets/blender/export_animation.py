import bpy
import struct

binfile = open('C:/temp/gdx/beatCity-android/assets/data/cubes.ani', 'wb')
    
frames = bpy.context.scene.frame_end - bpy.context.scene.frame_start
numberOfObjects = len(bpy.context.scene.objects)

factor = 5;

frames = int(frames / factor)

binfile.write(struct.pack('>i', numberOfObjects))
binfile.write(struct.pack('>i', frames))


for obj in bpy.context.scene.objects:    
    
    for frame in range(bpy.context.scene.frame_start, bpy.context.scene.frame_end):
        if (frame % factor != 0):
            continue 
        bpy.context.scene.frame_set(frame)
        
        binfile.write(struct.pack('>f', obj.location.x))
        binfile.write(struct.pack('>f', obj.location.y))
        binfile.write(struct.pack('>f', obj.location.z))
       
        binfile.write(struct.pack('>f', obj.rotation_euler.x))
        binfile.write(struct.pack('>f', obj.rotation_euler.y))
        binfile.write(struct.pack('>f', obj.rotation_euler.z))
        
        binfile.write(struct.pack('>f', obj.scale.x))
        binfile.write(struct.pack('>f', obj.scale.y))
        binfile.write(struct.pack('>f', obj.scale.z))
    
binfile.close