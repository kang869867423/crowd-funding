package org.fall.service.impl;

import org.apache.tomcat.jni.Address;
import org.fall.entity.po.AddressPO;
import org.fall.entity.po.AddressPOExample;
import org.fall.entity.po.OrderPO;
import org.fall.entity.po.OrderProjectPO;
import org.fall.entity.vo.AddressVO;
import org.fall.entity.vo.OrderProjectVO;
import org.fall.entity.vo.OrderVO;
import org.fall.mapper.AddressPOMapper;
import org.fall.mapper.OrderPOMapper;
import org.fall.mapper.OrderProjectPOMapper;
import org.fall.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired(required = false)
    OrderProjectPOMapper orderProjectPOMapper;

    @Autowired(required = false)
    AddressPOMapper addressPOMapper;

    @Autowired(required = false)
    OrderPOMapper orderPOMapper;



    public OrderProjectVO getOrderProjectVO(Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }


    public List<AddressVO> getAddressListVOByMemberId(Integer memberId) {

        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);

        List<AddressVO> addressVOList = new ArrayList();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }

        return addressVOList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)

    public void saveAddressPO(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insert(addressPO);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)

    public void saveOrder(OrderVO orderVO) {
        // ??????OrderPO??????
        OrderPO orderPO = new OrderPO();
        // ????????????OrderVO???OrderPO??????
        BeanUtils.copyProperties(orderVO,orderPO);
        // ???OrderPO???????????????
        orderPOMapper.insert(orderPO);
        // ??????????????????????????????order id
        Integer orderId = orderPO.getId();
        // ??????orderProjectVO
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        // ??????OrderProjectPO??????
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        // ??????
        BeanUtils.copyProperties(orderProjectVO,orderProjectPO);
        // ???orderProjectPO??????orderId
        orderProjectPO.setOrderId(orderId);
        // ???????????????
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
