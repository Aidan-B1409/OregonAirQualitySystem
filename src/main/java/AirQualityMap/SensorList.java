package AirQualityMap;

public enum SensorList {
    NWX_PurpleAir(15709.0),
    NW_Crossing(14791),
    West_Bend_Village(14473),
    Tethero(15979),
    Woodriver_Village(18853),
    SW_Bend(3340),
    Quiet_River_Lane(14607),
    Awbrey_Butte_NW(15859),
    Awbrey_Village(31207),
    Oregon_Observatory(28745),
    Caldera_Springs(14971),
    Deschutes_River_Ranch(15875),
    Redmond_Home(3192),
    Nutcracker_Drive(3541),
    Panoramic(23307);

    public final double IDRequest;
    SensorList(double value){
        this.IDRequest = value;
    }

}
