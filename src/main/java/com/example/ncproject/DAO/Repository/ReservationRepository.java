package com.example.ncproject.DAO.Repository;

import com.example.ncproject.DAO.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
        @Query("SELECT e FROM Reservation e WHERE e.PlaceId = (:placeId) AND e.startDateReser = (:startDateReser) ORDER BY e.startTimeReser")
        Collection<Reservation> findAllByPlaceIdAndStartDateReser(Integer placeId, Date startDateReser);

        @Query("SELECT e FROM Reservation e WHERE (e.UserId = (:userId)) AND (e.startDateReser >= (:curDate)) ORDER BY e.PlaceId, e.startDateReser")
        Collection<Reservation> findAllCurrentAndFuture(String userId, Date curDate);

        @Query("SELECT e FROM Reservation e WHERE e.UserId = (:userId) AND e.endDateReser <= (:curDate) ORDER BY e.PlaceId, e.startDateReser")
        Collection<Reservation> findAllEnded(String userId, Date curDate);
}
