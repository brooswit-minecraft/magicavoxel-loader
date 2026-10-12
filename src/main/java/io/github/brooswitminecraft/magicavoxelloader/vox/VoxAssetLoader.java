package io.github.brooswitminecraft.magicavoxelloader.vox;

import java.io.*;
import java.nio.*;
import java.util.*;

/** Strict reader for the first model in a MagicaVoxel 150 .vox stream. */
public final class VoxAssetLoader {
    public static final int MAX_DIMENSION = 16;
    public static final int MAX_VOXELS = 1024;
    private VoxAssetLoader() { }

    public static VoxelModel load(InputStream input) throws IOException {
        ByteBuffer bytes = ByteBuffer.wrap(input.readAllBytes()).order(ByteOrder.LITTLE_ENDIAN);
        require(bytes, 8, "header");
        if (bytes.getInt() != 0x20584f56 || bytes.getInt() != 150) throw new VoxFormatException("Expected MagicaVoxel VOX 150 stream");
        require(bytes, 12, "MAIN chunk");
        if (bytes.getInt() != 0x4e49414d || bytes.getInt() != 0) throw new VoxFormatException("Missing MAIN chunk");
        int children = bytes.getInt(); require(bytes, children, "MAIN children");
        int end = bytes.position() + children, w = -1, h = -1, d = -1; List<Voxel> voxels = null;
        while (bytes.position() < end) {
            require(bytes, 12, "chunk header"); int id = bytes.getInt(), content = bytes.getInt(), child = bytes.getInt();
            if (content < 0 || child < 0 || content + child > bytes.remaining()) throw new VoxFormatException("Invalid chunk length");
            int contentEnd = bytes.position() + content;
            if (id == 0x455a4953) { require(bytes, 12, "SIZE payload"); w=bytes.getInt(); h=bytes.getInt(); d=bytes.getInt(); validateSize(w,h,d); }
            else if (id == 0x495a5958) { if (w < 0) throw new VoxFormatException("XYZI precedes SIZE"); require(bytes, 4, "XYZI count"); int n=bytes.getInt(); if(n<0 || n>MAX_VOXELS) throw new VoxFormatException("Voxel count exceeds "+MAX_VOXELS); require(bytes, n*4, "XYZI voxels"); var list=new ArrayList<Voxel>(n); for(int i=0;i<n;i++){int x=Byte.toUnsignedInt(bytes.get()),y=Byte.toUnsignedInt(bytes.get()),z=Byte.toUnsignedInt(bytes.get()),p=Byte.toUnsignedInt(bytes.get()); if(x>=w||y>=h||z>=d||p==0) throw new VoxFormatException("Invalid voxel at index "+i); list.add(new Voxel(x,y,z,p));} voxels=List.copyOf(list); }
            bytes.position(contentEnd + child);
        }
        if (w < 0 || voxels == null) throw new VoxFormatException("A SIZE and XYZI chunk are required");
        return new VoxelModel(w,h,d,voxels);
    }
    private static void validateSize(int w,int h,int d) { if(w<1||h<1||d<1||w>MAX_DIMENSION||h>MAX_DIMENSION||d>MAX_DIMENSION||(long)w*h*d>MAX_VOXELS) throw new VoxFormatException("Dimensions must be 1.."+MAX_DIMENSION+" with at most "+MAX_VOXELS+" cells"); }
    private static void require(ByteBuffer b,int n,String what){ if(n<0||b.remaining()<n) throw new VoxFormatException("Truncated "+what); }
}
