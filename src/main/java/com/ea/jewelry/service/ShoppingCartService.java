package com.ea.jewelry.service;

import com.ea.jewelry.domain.*;
import com.ea.jewelry.repository.*;
import com.ea.jewelry.service.helper.ItemPriceHelper;
import com.ea.jewelry.web.rest.dto.ShoppingCartCustomerDTO;
import com.ea.jewelry.web.rest.dto.ShoppingCartDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShoppingCartService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private ShoppingCartRepository shoppingCartRepository;

    @Inject
    private ShoppingCartDetailsRepository shoppingCartDetailsRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserInformationRepository userInformationRepository;

    @Inject
    private ItemRepository itemRepository;


    public ShoppingCart addItemToShoppingCart(Long itemId, Long quantity) {
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUserIsCurrentUser();
        ShoppingCart shoppingCart = selectCorrectShoppingCart(shoppingCartList);
        List<ShoppingCartDetails> shoppingCartDetailsList = new ArrayList<>();
        Item item = itemRepository.findOne(itemId);

        for(int i = 0; i < quantity; i++) {
            ShoppingCartDetails shoppingCartDetails = new ShoppingCartDetails();
            shoppingCartDetails.setShoppingCart(shoppingCart);
            shoppingCartDetails.setItem(item);
            shoppingCartDetailsList.add(shoppingCartDetails);
        }

        shoppingCartDetailsRepository.save(shoppingCartDetailsList);

        return shoppingCart;
    }

    public ShoppingCart removeItemFromShoppingCart(Long itemId) {
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUserIsCurrentUser();
        ShoppingCart shoppingCart = selectCorrectShoppingCart(shoppingCartList);
        List<ShoppingCartDetails> shoppingCartDetailsList = shoppingCartDetailsRepository.findByShoppingCart(shoppingCart);
        List<ShoppingCartDetails> shoppingCartDetailsListRemovedItem = new ArrayList<>();
        Item item = itemRepository.findOne(itemId);

        shoppingCartDetailsList.forEach(
            list -> {
                if (list.getItem() == item){
                    shoppingCartDetailsListRemovedItem.add(list);
                }
            }
        );

        shoppingCartDetailsRepository.delete(shoppingCartDetailsListRemovedItem);

        return shoppingCart;
    }

    public ShoppingCartCustomerDTO getCurrent() {
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUserIsCurrentUser();
        ShoppingCart shoppingCart = selectCorrectShoppingCart(shoppingCartList);
        List<ShoppingCartDetails> shoppingCartDetailsList = shoppingCartDetailsRepository.findByShoppingCart(shoppingCart);
        UserInformation userInformation = userInformationRepository.findByUserIsCurrentUser();
        ShoppingCartCustomerDTO shoppingCartCustomerDTO = mapToShoppingCartDTO(shoppingCartDetailsList,userInformation);
        shoppingCartCustomerDTO.setTotalPrice(calculateShoppingCartTotal(shoppingCartCustomerDTO));
        return shoppingCartCustomerDTO;
    }

    public void cleanShoppingCart() {
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUserIsCurrentUser();
        ShoppingCart shoppingCart = selectCorrectShoppingCart(shoppingCartList);
        List<ShoppingCartDetails> shoppingCartDetailsList = shoppingCartDetailsRepository.findByShoppingCart(shoppingCart);
        shoppingCartDetailsRepository.delete(shoppingCartDetailsList);
        shoppingCartRepository.delete(shoppingCart);
    }

    private ShoppingCart selectCorrectShoppingCart(List<ShoppingCart> shoppingCartList) {
        User currentUser = userService.getUserWithAuthorities();
        ShoppingCart shoppingCart;

        if (shoppingCartList.isEmpty()) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(currentUser);
            shoppingCart = shoppingCartRepository.save(shoppingCart);
        } else {
            if (shoppingCartList.size() > 1) {
                log.debug("Multiple Shopping Cart for single user");
                shoppingCart = shoppingCartList.get(1);
            } else {
                shoppingCart = shoppingCartList.get(0);
            }
        }

        return shoppingCart;
    }

    private ShoppingCartCustomerDTO mapToShoppingCartDTO(List<ShoppingCartDetails> shoppingCartDetailsList,
                                                         UserInformation userInformation) {
        ShoppingCartCustomerDTO shoppingCartCustomerDTO = new ShoppingCartCustomerDTO();
        shoppingCartCustomerDTO.setUserInformation(userInformation);
        List<ShoppingCartDetailsDTO> shoppingCartDetailsDTOList = shoppingCartCustomerDTO.getShoppingCartDetailsList();

        for(int i = 0; i < shoppingCartDetailsList.size(); i++) {
            ShoppingCartDetails shoppingCartDetails = shoppingCartDetailsList.get(i);

            if (shoppingCartDetailsDTOList.isEmpty()) {
                shoppingCartDetailsDTOList.add(new ShoppingCartDetailsDTO(shoppingCartDetails.getItem()));
            } else {
                int dtoListSize = shoppingCartDetailsDTOList.size();
                boolean isItemEquals = Boolean.FALSE;

                for (int j = 0; j < dtoListSize; j++) {
                    ShoppingCartDetailsDTO listItemDTO = shoppingCartDetailsDTOList.get(j);
                    isItemEquals = listItemDTO.getItem().equals(shoppingCartDetails.getItem());

                    if (isItemEquals) {
                        listItemDTO.addItemQuantity();
                    }
                }

                if (!isItemEquals) {
                    shoppingCartDetailsDTOList.add(new ShoppingCartDetailsDTO(shoppingCartDetails.getItem()));
                }
            }
        }

        return shoppingCartCustomerDTO;
    }

    private Double calculateShoppingCartTotal(ShoppingCartCustomerDTO shoppingCartDTO) {
        final int priceTier = shoppingCartDTO.getUserInformation().getPriceTier();
        Double total = 0.0;

        List<ShoppingCartDetailsDTO> shoppingCartDetailsDTOList = shoppingCartDTO.getShoppingCartDetailsList();

        for(int i = 0; i < shoppingCartDetailsDTOList.size(); i++) {
            ShoppingCartDetailsDTO detailsDTO = shoppingCartDetailsDTOList.get(i);
            Price price = detailsDTO.getItem().getPrice();
            int quantity = detailsDTO.getQuantity();
            Double itemPrice = ItemPriceHelper.getPriceWithPriceTier(price,priceTier);
            total += itemPrice * quantity;
        }

        return total;
    }
}
