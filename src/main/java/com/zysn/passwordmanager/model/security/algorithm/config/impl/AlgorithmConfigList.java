package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import java.util.Map;

/**
 * Record that holds information about an algorithm.
 * 
 * <p>
 * This record contains the algorithm's name, type, and parameters. It uses
 * {@link AlgorithmConfigListParameters}.
 * </p>
 * 
 * @param algorithmName the name of the algorithm
 * @param algorithmType the type of the algorithm
 * @param parameters    a {@link Map} containing the parameters of the
 *                      algorithm, each associated with an
 *                      {@link AlgorithmConfigListParameters} object
 */
public record AlgorithmConfigList(String algorithmName, String algorithmType,
                Map<String, AlgorithmConfigListParameters> parameters) {

}
