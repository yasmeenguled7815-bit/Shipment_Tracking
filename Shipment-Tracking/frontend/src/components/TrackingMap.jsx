import {
  MapContainer,
  TileLayer,
  Marker,
  Popup
} from "react-leaflet";

import "leaflet/dist/leaflet.css";

import L from "leaflet";

const markerIcon = new L.Icon({
  iconUrl:
    "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",

  shadowUrl:
    "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",

  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});


function TrackingMap({ location }) {

  const defaultPosition = [
    12.9716,
    77.5946
  ];

  const position = location
    ? [
        location.latitude,
        location.longitude
      ]
    : defaultPosition;


  return (
    <MapContainer
      center={position}
      zoom={13}
      style={{
        height: "500px",
        width: "100%"
      }}
    >

      <TileLayer
        attribution="&copy; OpenStreetMap contributors"
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {location && (

        <Marker
          position={position}
          icon={markerIcon}
        >

          <Popup>

            <strong>
              Shipment #{location.shipmentId}
            </strong>

            <br />

            Latitude: {location.latitude}

            <br />

            Longitude: {location.longitude}

          </Popup>

        </Marker>

      )}

    </MapContainer>
  );
}

export default TrackingMap;