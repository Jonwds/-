<html>
<#include "../common/header.ftl">


<body>
<div id="wrapper" class="toggled">

<#-- 边栏 -->
        <#include "../common/nav.ftl">

<#--主要内容-->
    <form role="form">
        <div class="form-group">
            <label>名称</label>
            <input name="productName" type="text" class="form-control" value="${(prodcutInfo.productName)!''}"/>
        </div>
        <div class="form-group">
            <label>价格</label>
            <input name="productPrice" type="text" class="form-control" value="${(prodcutInfo.productPrice)!''}"/>
        </div>
        <div class="form-group">
            <label>库存</label>
            <input name="productStock" type="number" class="form-control" value="${(prodcutInfo.productStock)!''}"/>
        </div>
        <div class="form-group">
            <label>描述</label>
            <input name="productDescription" type="number" class="form-control" value="${(prodcutInfo.productDescription)!''}"/>
        </div>
        <div class="form-group">
            <label>图片</label>
            <img height="100" width="100" src="{(prodcutInfo.productIcon)!''">
            <input name="productIcon" type="image" class="form-control" value="${(prodcutInfo.productIcon)!''}"/>
        </div>
        <div class="form-group">
            <label>类目</label>
            <select name="categoryType" class="form-control">
                <#list categoryList as category>
                    <option value="${category.categoryType}"
                            <#if (productInfo.categoryType)?? && productInfo.categoryType==category.categoryType>
                            selected
                            </#if>
                        >${category.categoryName}
                    </option>
                </#list>
            </select>
        </div>


        <div class="checkbox">
            <label><input type="checkbox" />Check me out</label>
        </div> <button type="submit" class="btn btn-default">Submit</button>
    </form>


</div>



</body>

</html>