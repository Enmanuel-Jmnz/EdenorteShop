package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.entities.User;
import com.github.adminfaces.starter.ejb.UserFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserLazyDataModel extends LazyDataModel<User> {

    private final UserFacade userFacade;

    public UserLazyDataModel(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public List<User> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        // Manejo de ordenamiento
        String sortField = null;
        boolean ascending = true;

        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta sortMeta = sortBy.values().iterator().next(); 
            sortField = sortMeta.getField();
            ascending = sortMeta.getOrder() == SortOrder.ASCENDING;
        }

        // Verificar filtros
        Map<String, Object> filters = filterBy.entrySet().stream()
                .filter(entry -> entry.getValue().getFilterValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        entry -> entry.getValue().getFilterValue()
                ));

        // Llamada al método del facade para obtener la paginación con filtros
        List<User> users = userFacade.findUsers(first, pageSize, sortField, ascending, filters);

        // Establecer el total de registros después del filtrado
        setRowCount(userFacade.count(filters));
        
        return users;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        // Convertir los filtros para contar registros
        Map<String, Object> filters = filterBy.entrySet().stream()
                .filter(entry -> entry.getValue().getFilterValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        entry -> entry.getValue().getFilterValue()
                ));

        // Llamada al método del facade para contar registros filtrados
        return userFacade.count(filters);
    }
}
