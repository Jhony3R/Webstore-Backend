package jhony.ruiz.sigevi.service;

import jhony.ruiz.sigevi.model.Usuario;

public interface IUsuarioService extends ICRUD<Usuario, Integer> {
    void desactivar(Integer id);
    void reactivar(Integer id);
    Usuario findByUsername(String username);
}
