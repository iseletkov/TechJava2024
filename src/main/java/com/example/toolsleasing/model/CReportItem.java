package com.example.toolsleasing.model;

public class CReportItem extends CTool
{
    public CReportItem(
        Long id,
        String name,
        Double price,
        Long totalLeaseCount
    )
    {
        super(id, name, price);
        setTotalLeaseCount(totalLeaseCount);
    }
    private Long totalLeaseCount;

    public Long getTotalLeaseCount() {
        return totalLeaseCount;
    }

    public void setTotalLeaseCount(Long totalLeaseCount) {
        if (totalLeaseCount>=0)
            this.totalLeaseCount = totalLeaseCount;
    }
}
