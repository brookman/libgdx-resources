import bpy
import random

bpy.context.scene.frame_set(120)

for obj in bpy.context.scene.objects:
    if obj.select:
        obj.keyframe_insert(data_path="rotation_euler", frame=160.0, index=0)
        obj.keyframe_insert(data_path="rotation_euler", frame=160.0, index=1)
        obj.keyframe_insert(data_path="rotation_euler", frame=160.0, index=2)
        obj.rotation_euler.x = random.random() * 20-10
        obj.rotation_euler.y = random.random() * 20-10
        obj.rotation_euler.z = random.random() * 20-10
        obj.keyframe_insert(data_path="rotation_euler", frame=60.0, index=0)
        obj.keyframe_insert(data_path="rotation_euler", frame=60.0, index=1)
        obj.keyframe_insert(data_path="rotation_euler", frame=60.0, index=2)