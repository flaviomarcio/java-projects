package com.app.Tests;

import com.app.business.dto.Command;
import com.app.business.dto.Target;
import com.app.business.model.ofservice.CommanderModel;
import com.app.business.service.CommanderService;
import com.app.factory.FactoryByTests;
import com.littlecode.containers.ObjectReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CommanderTest {

    private final FactoryByTests FACTORY = new FactoryByTests();
    private final CommanderService CRUD = FACTORY.getCommanderService();
    private final List<CommanderModel> CRUDList = FACTORY.getCommanderModelList();

    private boolean dataValidation(Target target){
        Assertions.assertNotNull(target);
        Assertions.assertNotNull(target.getId());
        Assertions.assertNotNull(target.getDtChange());
        return true;
    }

    @Test
    public void UT_000_CHECK_SAVE() {

        for (var model : CRUDList) {

            var in = CRUD.get(model.getId()).cast(Target.class);
            Assertions.assertNotNull(in);
            Assertions.assertNotNull(in.getId());
            Assertions.assertEquals(in.getId(), model.getId());
            Assertions.assertNotNull(in.getDtChange());

            Assertions.assertNotNull(CRUD.saveIn(in));
            in.setId(null);
            in.setDtChange(null);
            var outIn = CRUD.saveIn(in);
            Assertions.assertTrue(outIn.isOK());
            Assertions.assertTrue(this.dataValidation(outIn.cast(Target.class)));

        }
    }

    @Test
    public void UT_000_CHECK_DELETE() {
        for (var model : CRUDList) {
            var in = CRUD.get(model.getId()).cast(Target.class);
            Assertions.assertTrue(CRUD.delete(in.getId()).isOK());
            Assertions.assertTrue(CRUD.delete(in.getId()).isOK());
            Assertions.assertEquals(CRUD.delete((String) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.delete("").getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.delete((UUID) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.delete(in).getType(), ObjectReturn.Type.Success);
            Assertions.assertEquals(CRUD.delete(UUID.randomUUID()).getType(), ObjectReturn.Type.NoContent);
            in.setId(null);
            Assertions.assertEquals(CRUD.delete(in).getType(), ObjectReturn.Type.BadRequest);
        }
    }


}
