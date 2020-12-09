package com.example.cooplas.AgoraClasses;

public interface Packable {
    ByteBuf marshal(ByteBuf out);
}