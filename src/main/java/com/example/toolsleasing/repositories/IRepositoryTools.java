package com.example.toolsleasing.repositories;

import com.example.toolsleasing.model.CReportItem;
import com.example.toolsleasing.model.CTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositoryTools extends JpaRepository<CTool, Long> {
    //CrudRepository

//    https://www.bezkoder.com/jpa-native-query/
    @Query(
            value = """
                select
                    temp.id,
                    temp.name,
                    temp.price,
                    sum(temp.lease_days) as total_lease_days
                from
                (
                    SELECT
                        t.id as id,
                        t.name as name,
                        t.price as price,
                        COALESCE((l.date_to-l.date_from+1), 0) AS lease_days
                    FROM public.tools t
                    LEFT JOIN public.leases l
                    ON l.tool_id=t.id
                ) temp
                GROUP BY temp.id, temp.name, temp.price
                ORDER BY total_lease_days DESC
                LIMIT 5;
            """,
            nativeQuery = true)
    List<CReportItem> topPopularTools();
}
