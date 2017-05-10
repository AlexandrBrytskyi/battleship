package org.battleship.controller.user.remote.responses;

import java.io.Serializable;

/**
 * adds possibility to send request result in simple way
 */
public interface ICallback extends Serializable {


    void sendCallback();


}