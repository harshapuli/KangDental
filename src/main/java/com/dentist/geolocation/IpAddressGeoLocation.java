package com.dentist.geolocation;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

@Service
public class IpAddressGeoLocation {
	@Autowired
	@Qualifier("lookupService")
	private LookupService lookUpService;
	private static final Logger LOGGER = Logger.getLogger(IpAddressGeoLocation.class);

	public IpAddressGeoLocation() {

	}

	public void setLookUpService(LookupService lookUpService) {
		this.lookUpService = lookUpService;
	}

	public ServerLocation getLocation(String IPAddress) {
		ServerLocation serviceLocation = new ServerLocation();
		Location location = lookUpService.getLocation(IPAddress);
		try {

			serviceLocation.setCountryCode(location.countryCode);
			serviceLocation.setCountryName(location.countryName);
			serviceLocation.setRegionCode(location.region);
			serviceLocation.setRegionName(regionName.regionNameByCode(location.countryCode, location.region));
			serviceLocation.setCity(location.city);
			serviceLocation.setPostalCode(location.postalCode);
			serviceLocation.setLatitude(String.valueOf(location.latitude));
			serviceLocation.setLongitude(String.valueOf(location.longitude));
		} catch (Exception e) {
			LOGGER.info("Unable to get the location for given ip address");
			LOGGER.error("Invalid Ip Address : " + IPAddress, e);
		}

		return serviceLocation;
	}
}
