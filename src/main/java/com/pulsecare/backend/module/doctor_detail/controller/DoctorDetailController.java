package com.pulsecare.backend.module.doctor_detail.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.doctor_detail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctor_detail.dto.DoctorDetailResDto;

import java.util.List;

public interface DoctorDetailController extends
        FindableController<ResponseBody<DoctorDetailResDto>, ResponseBody<List<DoctorDetailResDto>>, Long>,
        CreatableController<DoctorDetailReqDto, ResponseBody<DoctorDetailResDto>>,
        UpdatableController<DoctorDetailReqDto, ResponseBody<DoctorDetailResDto>, String>,
        DeletableController<ResponseBody<String>, Long> {
}
