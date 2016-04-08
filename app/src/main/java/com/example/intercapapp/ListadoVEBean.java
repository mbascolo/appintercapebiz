package com.example.intercapapp;

import java.util.List;

/**
 * Created by Matias on 08/04/2016.
 */
public class ListadoVEBean {

    /**
     * EJEMPLO
     * nroPool : 388
     * descripcion : Second Brand - BUE
     * pathImagenBannerMiniatura :
     * FIN EJEMPLO


    private List<PoolBean> pool;

    public List<PoolBean> getPool() {
        return pool;
    }

    public void setPool(List<PoolBean> pool) {
        this.pool = pool;
    }

    public static class PoolBean {*/
        private String nroPool;
        private String descripcion;
        private String pathImagenBannerMiniatura;

        public String getNroPool() {
            return nroPool;
        }

        public void setNroPool(String nroPool) {
            this.nroPool = nroPool;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getPathImagenBannerMiniatura() {
            return pathImagenBannerMiniatura;
        }

        public void setPathImagenBannerMiniatura(String pathImagenBannerMiniatura) {
            this.pathImagenBannerMiniatura = pathImagenBannerMiniatura;
       // }
    }
}
