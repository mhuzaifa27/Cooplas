package com.example.cooplas.AgoraClasses;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
