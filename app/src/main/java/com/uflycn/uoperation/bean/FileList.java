package com.uflycn.uoperation.bean;

import java.util.List;

public class FileList {
    private List<TowerHead> TowerHead;
    private List<TowerBrand> TowerBrand;
    private List<GroundingLead> GroundingLead;
    private List<PullLine> PullLine;

    public List<TowerHead> getTowerHead() {
        return TowerHead;
    }

    public void setTowerHead(List<TowerHead> TowerHead) {
        this.TowerHead = TowerHead;
    }

    public List<TowerBrand> getTowerBrand() {
        return TowerBrand;
    }

    public void setTowerBrand(List<TowerBrand> TowerBrand) {
        this.TowerBrand = TowerBrand;
    }

    public List<GroundingLead> getGroundingLead() {
        return GroundingLead;
    }

    public void setGroundingLead(List<GroundingLead> GroundingLead) {
        this.GroundingLead = GroundingLead;
    }

    public List<PullLine> getPullLine() {
        return PullLine;
    }

    public void setPullLine(List<PullLine> PullLine) {
        this.PullLine = PullLine;
    }
}
