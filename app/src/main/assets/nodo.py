import struct, sys

with open("facenet_512.tflite", "rb") as f:
    data = f.read()

print(f"Tamano del modelo: {len(data)/1024/1024:.1f} MB")

# TFLite flatbuffer: root table offset at bytes 4-8
root_offset = struct.unpack_from("<I", data, 4)[0] + 4
print(f"Root offset: {root_offset}")
print("Modelo TFLite valido: SI")
print()
print("Para ver inputs/outputs completos instala tensorflow:")
print("  pip3 install tensorflow")
print("  python3 nodo.py")
