package GTL_API.Services;

import GTL_API.Models.ReturnModels.CoverTypeReturn;
import GTL_API.Models.UpdateModels.CoverTypeUpdate;


public interface ICoverTypeService {
    CoverTypeReturn findCoverTypeByName(String coverType);

    CoverTypeReturn updateCoverType(CoverTypeUpdate coverType);
}
