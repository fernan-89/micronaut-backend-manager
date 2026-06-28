# 🚀 Enterprise IT Asset & SRE Compute Infrastructure API

## 📖 Overview
The **Enterprise IT Asset & SRE Compute Infrastructure API** is a high-performance, production-grade microservice architecture developed with **Micronaut** and **MongoDB**. It delivers an integrated ecosystem split into two core business domains:
1. **Asset Management (ITAM)**: Lifecycle control of heterogeneous corporate hardware items (Workstations, Notebooks, Mobile Devices, Monitors, and IoT).
2. **SRE Compute Infrastructure Governance**: Provisioning, structural orchestration, and telemetry tracking of hypervisors, bare-metal nodes, and execution layers across local home-labs, private datacenters, and public clouds.

---

## 🏗 Architectural Pillars

* **Dual-Domain Mapping**: Decouples physical hardware specs (warranty, manufacturer data) from computational capability (vCPUs, RAM, networks) while maintaining cross-domain correlation through unique entity hashes (`linkedAssetHash`).
* **Centralized Audit Architecture**: Change log history is decoupled from main entity documents and stored in a dedicated `logs-management` collection. This prevents document bloat, eliminates performance degradation on read operations, and guarantees immutable audit trails.
* **High-Fidelity Tracking**: Every mutation records a detailed structural "from/to" (De/Para) matrix, enabling deterministic rollback planning and real-time state reconstruction.
* **Strict Taxonomy Enforcement**: Employs absolute, case-insensitive compile-time Enum tokens with auto-trimming wire sanitization via `@Serdeable` to reject unrecognized layer targets or locations immediately at the serialization boundary.

---

## 🛠 Tech Stack
* **Framework**: Micronaut 4.x (Highly optimized reflection-free architecture)
* **Language**: Java 21 LTS
* **Database**: MongoDB (via Micronaut Data Reactive)
* **Serialization**: Micronaut Serde (Native JSON parsing via `@Serdeable`)
* **Automation Tools**: Newman CLI & Postman Runtime Engine

---

## 📐 Core Structural Taxonomies (Enums)

To guarantee database consistency and eliminate data drifting, the system enforces strict Enum schemas via native serialization blocks.

### 1. Equipment Type (`Asset Domain` - `EquipmentType`)
Defines the structural taxonomy classifications of physical corporate hardware items:
* `LAPTOP`: Portable employee workstations (e.g., ThinkPads, MacBooks).
* `DESKTOP`: Fixed desk workstations or mini PC hosts (e.g., ThinkCentre M75n).
* `MOBILE`: Corporate mobile cellular nodes or tablets (e.g., ThinkPhone, Edge 50 Pro).
* `NETWORKING`: Infrastructure hardware units (Switches, physical routers, firewalls, APs).
* `SERVER`: On-premise infrastructure hosts or rack-mounted computational blocks (e.g., ThinkStation P520).

### 2. Resource Type (`SRE Compute Domain` - `ResourceType`)
Defines the compute virtualization level and hypervisor execution architecture layer:
* `BARE_METAL_HOST`: Physical enterprise hardware server rack units hosting hypervisors or bare cluster engines.
* `VIRTUAL_MACHINE`: Traditional hypervisor-managed abstraction layer instances (e.g., AWS EC2, Proxmox VM).
* `CONTAINER`: Lightweight OS-level virtualized isolate processing nodes (e.g., Docker, LXD Container, Pod).

### 3. Infrastructure Provider (`SRE Compute Domain` - `InfrastructureProvider`)
Defines the physical or architectural hosting target zones:
* `AWS_CLOUD` / `AZURE_CLOUD` / `GCP_CLOUD`: Public cloud hyper-scalers.
* `PRIVATE_DATACENTER`: On-premises Tier-3/Tier-4 physical datacenters.
* `HOME_LAB`: Localized edge home-lab deployment ecosystems running behind custom residential networks.

---

## 🔑 API Endpoints Reference

### 📦 Asset Management Module (`/assets`)
| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/assets` | Ingests a new corporate hardware asset item (Validates `EquipmentType`). |
| `GET` | `/assets/{assetHash}` | Retrieves specific hardware profiling details and telemetry status. |
| `GET` | `/assets/search` | Dynamic lookup queries filtering assets by model, manufacturer, or serial. |
| `PUT` | `/assets/{assetHash}` | Performs field-level updates, logging modifications to the audit collection. |
| `DELETE` | `/assets/{assetHash}` | Permanently removes the asset record from the main inventory tracking. |

### ☸️ SRE Infrastructure Module (`/compute`)
| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/compute` | Provisions a new compute engine execution layer (Validates `ResourceType`/`InfrastructureProvider`). |
| `GET` | `/compute/{resourceHash}` | Fetches current orchestration specifications and linked physical asset hashes. |
| `GET` | `/compute/search` | Searches compute resources by IP addresses, hypervisors, or active hostnames. |
| `PUT` | `/compute/{resourceHash}` | Triggers partial infrastructure updates and recalculates processing deltas. |
| `DELETE` | `/compute/{resourceHash}` | Deprovisions the target execution layer and marks resources as unassigned. |

### 📜 Centralized Audit Log Module (`/logs`)
| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/assets/{assetHash}/logs` | Extracts complete historic audit logs and telemetry changes for a hardware asset. |
| `GET` | `/compute/{resourceHash}/logs` | Extracts complete historic delta calculations and execution state changes for a compute node. |

---

## 📝 Audit Strategy (De/Para Structure)

Our audit strategy relies on capturing data mutations dynamically. Instead of storing massive raw strings, mutations are parsed as field-level deltas inside the `changes` sub-document:

```json
"changes": {
    "operatingSystem": {
        "from": "Ubuntu 24.04 LTS",
        "to": "Ubuntu 26.04 LTS"
    },
    "status": {
        "from": "PROVISIONING",
        "to": "ACTIVE"
    }
}

```

* **Why?**: This structure allows front-end dashboards to easily render visual "De/Para" diffs and timelines without processing complex string manipulation at runtime.

---

## 🧪 Testing & Ingestion Automation Suite

The project includes an enterprise-grade automated quality assurance suite located inside the `./PostMan-Collection` directory. This suite contains Postman collections and environments designed for end-to-end sandbox validation, load pre-checking, and CI/CD ingestion pipelines.

### 📂 Directory Structure

```bash
./PostMan-Collection
├── Dev.postman_environment.json                     # Active environment variables (BaseURLs & Hashes)
├── Bulk_Asset_Ingestion_Suite.json                  # Ingests real hardware inventory (Workstations, Phones, IoT)
└── Infrastructure_De_Para_Audit_Validation.json     # Validates field-level "from/to" delta changes

```

### ⚙️ Headless Automation via Newman CLI

The entire collection can be run headlessly inside deployment pipelines (CI/CD) or locally using **Newman**. This guarantees that zero regression bugs reach the production cluster.

#### Prerequisites

```bash
npm install -g newman
npm install -g newman-reporter-htmlextra

```

#### Local Execution Pipeline:

```bash
newman run ./PostMan-Collection/Bulk_Asset_Ingestion_Suite.json \
  -e ./PostMan-Collection/Dev.postman_environment.json \
  --bail

```

#### Execution and HTML SRE Report Generation:

```bash
newman run ./PostMan-Collection/Bulk_Asset_Ingestion_Suite.json \
  -e ./PostMan-Collection/Dev.postman_environment.json \
  -r htmlextra --reporter-htmlextra-export ./reports/ingestion-report.html

```

---

## 📊 Postman Ingestion Pipeline Flow Example

When deploying a localized hypervisor machine like a Lenovo ThinkStation P520, the Postman runner coordinates an asynchronous dual-domain provisioning flow:

1. **Physical Registration (`POST /assets`)**: Creates the physical asset entry with type `SERVER` based on the underlying hardware profile.
2. **Dynamic Context Binding**: Postman JavaScript test block captures the API response and automatically populates the `{{currentAssetHash}}` environment variable.
3. **SRE Provisioning (`POST /compute`)**: Creates the infrastructure layer using `BARE_METAL_HOST` as `resourceType` under the `HOME_LAB` provider, linking the hypervisor details (`LXD Engine`), management URL (`https://192.168.1.100:8443/`), and the dynamic `linkedAssetHash` seamlessly.

---

## 💡 Best Practices for Contributors

1. **Header Requirement**: Always provide the `X-Executor` header on mutation requests (`POST`, `PUT`, `DELETE`) to identify the agent, automated pipeline, or engineer performing the operation.
2. **Input Token Integrity**: Pay close attention to Enums. The Micronaut serialization engine uses absolute uppercase matching. Sending `BARE_METAL_SERVER` instead of `BARE_METAL_HOST` or `ON_PREMISE` instead of `HOME_LAB` will result in an immediate `BusinessException` wire block.
3. **Partial Updates**: The API architecture gracefully processes partial deltas. Only transmit fields targeting explicit change requirements.
4. **Newman Gatekeeping**: Every pull request triggers a GitHub Action runner that spins up an ephemeral MongoDB context, compiles the Micronaut codebase, and forces an execution pass through `newman run`. Ensure all local assertions pass green before opening a PR.

```