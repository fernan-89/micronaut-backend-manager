package com.local.app.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

/**
 * Embedded record representing structural hardware specifications and networking identities.
 * Directly nested within the asset document inside MongoDB.
 */
@Serdeable
@Introspected
public record AssetSpecificationModel( // <-- Mudado aqui para bater com o arquivo
                                       /**
                                        * The hardware manufacturing company (e.g., Apple, Dell, Lenovo).
                                        */
                                       @Nullable
                                       String manufacturer,

                                       /**
                                        * The specific commercial model name or number (e.g., MacBook Pro M3, ThinkPad X1).
                                        */
                                       @Nullable
                                       String model,

                                       /**
                                        * The unique hardware Media Access Control address of the primary network interface.
                                        */
                                       @Nullable
                                       String macAddress,

                                       /**
                                        * The last known internet protocol address assigned to the host machine.
                                        */
                                       @Nullable
                                       String ipAddress,

                                       /**
                                        * The operational platform version installed on the physical node.
                                        */
                                       @Nullable
                                       String operatingSystem
) {}