import { useEffect, useMemo, useState } from "react";
import { MapContainer, TileLayer, Marker, Polyline, Popup, useMap } from "react-leaflet";
import L from "leaflet";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

// Change these if your backend uses a different address or WebSocket endpoint.
const API_BASE = "http://localhost:8080";
const WS_URL = `${API_BASE}/ws`;

const initialShipments = [
    { 
		id: 1, code: "SHP-1001", 
		source: "Bengaluru", 
		destination: "Pune", 
		weight: 500, 
		status: "IN_TRANSIT", 
		latitude: 12.9716, 
		longitude: 77.5946, 
		value: 125000 
	},
    { 
		id: 2, 
		code: "SHP-1002", 
		source: "Chennai", 
		destination: "Hyderabad", 
		weight: 750, status: "PENDING", 
		latitude: 13.0827, 
		longitude: 80.2707, 
		value: 98000 
	},
    {
		 id: 3,
		  code: "SHP-1003", 
		  source: "Mumbai", 
		  destination: "Goa", 
		  weight: 300, 
		  status: "AWAITING_PICKUP", 
		  latitude: 19.076, 
		  longitude: 72.8777, 
		  value: 65000 
	  }
];

const markerIcon = L.divIcon({
    className: "shipment-marker-wrap",
    html: '<div class="shipment-marker"><span>➤</span></div>',
    iconSize: [34, 34],
    iconAnchor: [17, 17]
});

function MapResize({ selected }) {
    const map = useMap();
    useEffect(() => {
        map.invalidateSize();
        if (selected?.latitude && selected?.longitude) {
            map.flyTo([selected.latitude, selected.longitude], 7, { duration: 0.7 });
        }
    }, [map, selected]);
    return null;
}

function StatCard({ icon, label, value, hint, tone }) {
    return (
        <div className="stat-card">
            <div className={`stat-icon ${tone}`}>{icon}</div>
            <div className="stat-label">{label}</div>
            <div className="stat-value">{value}</div>
            <div className="stat-hint">{hint}</div>
        </div>
    );
}

export default function App() {
    const [shipments, setShipments] = useState(initialShipments);
    const [selectedId, setSelectedId] = useState(1);
    const [search, setSearch] = useState("");
    const [statusFilter, setStatusFilter] = useState("ALL");
    const [view, setView] = useState("map");
    const [connection, setConnection] = useState("Connecting");
    const [notice, setNotice] = useState("");

    const selected = shipments.find((shipment) => shipment.id === selectedId) || shipments[0];

    const filteredShipments = useMemo(() => shipments.filter((shipment) => {
        const text = `${shipment.code} ${shipment.source} ${shipment.destination}`.toLowerCase();
        return text.includes(search.toLowerCase()) &&
            (statusFilter === "ALL" || shipment.status === statusFilter);
    }), [shipments, search, statusFilter]);

    // Subscribe to the selected shipment's location topic whenever selection changes.
    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS(WS_URL),
            reconnectDelay: 5000,
            onConnect: () => {
                setConnection("Live");
                client.subscribe(`/topic/shipment/${selectedId}`, (message) => {
                    try {
                        const location = JSON.parse(message.body);
                        const latitude = Number(location.latitude);
                        const longitude = Number(location.longitude);
                        if (!Number.isFinite(latitude) || !Number.isFinite(longitude)) return;
                        setShipments((current) => current.map((shipment) =>
                            shipment.id === selectedId ? 
						{ ...shipment, latitude, longitude, status: "IN_TRANSIT" } : shipment
                        ));
                    } catch {
                        setNotice("Received a location update that could not be read.");
                    }
                });
            },
            onStompError: () => setConnection("Disconnected"),
            onWebSocketClose: () => setConnection("Reconnecting")
        });
        client.activate();
        return () => client.deactivate();
    }, [selectedId]);

    const totalWeight = shipments.reduce((sum, shipment) => sum + Number(shipment.weight || 0), 0);
    const totalValue = shipments.reduce((sum, shipment) => sum + Number(shipment.value || 0), 0);

    async function loadShipments() {
        try {
            // Your backend may require a JWT. Add Authorization: `Bearer ${token}` when available.
            const response = await fetch(`${API_BASE}/shipper/shipments`);
            if (!response.ok) throw new Error(`API returned ${response.status}`);
            const data = await response.json();
            if (Array.isArray(data) && data.length) {
                setShipments(data.map((item, index) => ({
                    ...item,
                    id: item.id ?? index + 1,
                    code: item.code ?? `SHP-${item.id ?? index + 1}`,
                    source: item.source ?? "Unknown origin",
                    destination: item.destination ?? "Unknown destination",
                    weight: item.weight ?? 0,
                    status: item.status ?? "PENDING",
                    latitude: item.latitude ?? 12.9716,
                    longitude: item.longitude ?? 77.5946,
                    value: item.value ?? 0
                })));
                setNotice("Shipments loaded from the backend.");
            } else {
                setNotice("Backend responded, but no shipments were returned. Showing demo data.");
            }
        } catch (error) {
            setNotice(`Could not load API data (${error.message}). Showing demo data.`);
        }
    }

    return (
        <div className="app-shell">
            <aside className="sidebar">
                <div className="brand"><div className="brand-mark">S</div><span>Ship<span className="brand-light">Flow</span></span></div>
                <div className="side-caption">WORKSPACE</div>
                <button className="nav-item"><span>▦</span> Dashboard</button>
                <button className="nav-item active">
				<span>▣</span> Shipments <span className="nav-count">{shipments.length}</span></button>
                <button className="nav-item" onClick={() => setNotice("Tracking view is shown on the map.")}>
				<span>⌖</span> Live tracking</button>
                <button className="nav-item" onClick={() => setNotice("Invoice module can be connected next.")}>
				<span>▤</span> Invoices</button>
                <button className="nav-item" onClick={() => setNotice("Product module can be connected next.")}>
				<span>□</span> Products</button>
                <div className="sidebar-bottom">
				<div className="avatar">Y</div><div><strong>Workspace User</strong>
				<small>Shipment operations</small></div><span className="more">•••</span></div>
            </aside>

            <main className="main-content">
                <header className="topbar">
                    <div><div className="eyebrow">OPERATIONS / SHIPMENTS</div>
					<h1>Shipment Tracking</h1>
					<p className="subtitle">Monitor your shipments and deliveries in real time.</p></div>
                    <div className="top-actions">
                        <div className={`connection ${connection === "Live" ? "connected" : ""}`}><span className="connection-dot" /> {connection}</div>
                        <button className="icon-button" title="Refresh shipments" onClick={loadShipments}>↻</button>
                        <button className="primary-button" onClick={() => setNotice("Connect this button to your create-shipment form/API.")}>＋ Add shipment</button>
                    </div>
                </header>

                {notice && <div className="notice"><span>{notice}</span><button onClick={() => setNotice("")}>×</button></div>}

                <section className="stats-grid">
                    <StatCard icon="▣" label="Total shipments" value={shipments.length.toString().padStart(2, "0")} hint="Across all shipment statuses" tone="purple" />
                    <StatCard icon="⌁" label="Total distance" value="1,240 km" hint="Estimated route distance" tone="green" />
                    <StatCard icon="▤" label="Total weight" value={`${totalWeight.toLocaleString()} kg`} hint="Combined shipment weight" tone="orange" />
                    <StatCard icon="₹" label="Total shipment value" value={`₹${(totalValue / 1000).toFixed(0)}k`} hint="Estimated cargo value" tone="blue" />
                </section>

                <section className="content-grid">
                    <div className="panel shipment-panel">
                        <div className="panel-heading">
						<div><h2>Shipments</h2>
						<p>Manage and track your deliveries</p></div>
						<span className="count-pill">{filteredShipments.length} results</span></div>
                        <div className="filters">
                            <div className="search-box">
							<span>⌕</span>
							<input value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search shipment..." /></div>
                            <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)} aria-label="Filter by status">
                                <option value="ALL">All statuses</option>
								<option value="PENDING">Pending</option><option value="AWAITING_PICKUP">Awaiting pickup</option><option value="IN_TRANSIT">In transit</option>
                            </select>
                        </div>
                        <div className="shipment-list">
                            {filteredShipments.map((shipment) => (
                                <button key={shipment.id} className={`shipment-row ${selectedId === shipment.id ? "selected" : ""}`} onClick={() => setSelectedId(shipment.id)}>
                                    <div className="shipment-row-top">
									<span className="shipment-code">{shipment.code}</span><span className={`status status-${shipment.status.toLowerCase()}`}>{shipment.status.replaceAll("_", " ")}</span></div>
                                    <div className="route-line">
									<span className="route-dot origin" /><div>
									<strong>{shipment.source}</strong>
									<small>Origin</small></div>
									<div className="route-dash" />
									<span className="route-dot destination" />
									<div><strong>{shipment.destination}</strong>
									<small>Destination</small></div></div>
                                    <div className="row-meta">
									<span>◉ {shipment.weight} kg</span>
									<span>Shipment #{shipment.id}</span></div>
                                </button>
                            ))}
                            {!filteredShipments.length && <div className="empty-state">No shipments match your search.</div>}
                        </div>
                        <button className="load-button" onClick={loadShipments}>Load from backend ↗</button>
                    </div>

                    <div className="panel map-panel">
                        <div className="panel-heading map-heading"><div><h2>Live shipment map</h2><p>{selected ? `${selected.code} · ${selected.source} → ${selected.destination}` : "Select a shipment"}</p></div><div className="view-switch"><button className={view === "map" ? "chosen" : ""} onClick={() => setView("map")}>Map view</button><button className={view === "details" ? "chosen" : ""} onClick={() => setView("details")}>Details</button></div></div>
                        {view === "map" ? (
                            <div className="map-wrap">
                                <MapContainer center={[selected?.latitude || 12.9716, selected?.longitude || 77.5946]} zoom={5} scrollWheelZoom className="leaflet-map">
                                    <TileLayer attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>' url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                                    <MapResize selected={selected} />
                                    {shipments.map((shipment) => (
                                        <Marker key={shipment.id} position={[shipment.latitude || 12.9716, shipment.longitude || 77.5946]} icon={markerIcon} eventHandlers={{ click: () => setSelectedId(shipment.id) }}>
                                            <Popup><strong>{shipment.code}</strong><br />{shipment.source} → {shipment.destination}<br />Status: {shipment.status}</Popup>
                                        </Marker>
                                    ))}
                                    {selected && <Polyline positions={[[selected.latitude || 12.9716, selected.longitude || 77.5946], [18.5204, 73.8567]]} pathOptions={{ color: "#4568f5", weight: 4, dashArray: "8 8" }} />}
                                </MapContainer>
                                <div className="map-legend">
								<span><i className="legend-blue" /> Selected shipment</span>
								<span>
								<i className="legend-grey" /> Other shipments</span></div>
                            </div>
                        ) : (
                            <div className="details-view">
                                <div className="details-icon">▣</div>
								<h3>{selected?.code}</h3>
								<span className={`status status-${selected?.status.toLowerCase()}`}>{selected?.status.replaceAll("_", " ")}</span>
                                <div className="detail-grid">
								<div><small>Origin</small>
								<strong>{selected?.source}</strong>
								</div><div><small>Destination</small>
								<strong>{selected?.destination}</strong></div><div>
								<small>Weight</small>
								<strong>{selected?.weight} kg</strong></div><div>
								<small>Shipment ID</small>
								<strong>#{selected?.id}</strong></div><div>
								<small>Latest latitude</small><strong>{selected?.latitude?.toFixed(4)}</strong></div><div>
								<small>Latest longitude</small><strong>{selected?.longitude?.toFixed(4)}</strong></div></div>
                                <p className="details-note">Location values update when your backend publishes a message to <code>/topic/shipment/{selectedId}</code>.</p>
                            </div>
                        )}
                        <div className="map-footer">
						<div><span className="live-pulse" /> <strong>Location updates</strong>
						<small>Waiting for GPS messages</small></div>
						<div className="coordinates">
						<small>LATITUDE</small><strong>{selected?.latitude?.toFixed(4) || "—"}</strong></div>
						<div className="coordinates">
						<small>LONGITUDE</small><strong>{selected?.longitude?.toFixed(4) || "—"}</strong></div></div>
                    </div>
                </section>
                <footer className="footer">
				<span>ShipFlow · Shipment Operations</span>
				<span>React · Spring Boot · WebSocket · Leaflet</span></footer>
            </main>
        </div>
    );
}