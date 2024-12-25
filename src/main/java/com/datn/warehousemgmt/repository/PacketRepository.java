package com.datn.warehousemgmt.repository;

import com.datn.warehousemgmt.entities.Packet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacketRepository extends JpaRepository<Packet, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM Packet WHERE rfidTag = ?1")
    Boolean checkIfAlreadyImport(String rfidTag);

    @Query(value = "SELECT pl.packets FROM ProductsLog pl WHERE pl.id = ?1")
    List<Packet> getPacketList(Long productLogId);

    Packet findByRfidTag(String rfidTag);
}
