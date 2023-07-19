    package com.example.aplikasi_interior;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import java.io.ByteArrayOutputStream;
    import android.graphics.Bitmap;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.Context;
    import android.content.Intent;

    import java.util.List;

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private List<Product> productList;
        private Context context;

        public ProductAdapter(List<Product> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            final Product product = productList.get(position);

            // Set the image bitmap to the ImageView
            holder.productImage.setImageBitmap(product.getImage());

            // Set the other views as needed
            holder.productName.setText(product.getName());
            holder.productPrice.setText(product.getPrice());

            // Set a click listener on the item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create an intent to launch ProductDetailActivity
                    Intent intent = new Intent(context, ProductDetailActivity.class);

                    // Convert the image Bitmap to byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    product.getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    // Set the byte array to the Product object
                    product.setImageBytes(byteArray);

                    // Pass the Product object through the intent
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }
        public class ProductViewHolder extends RecyclerView.ViewHolder {
            private ImageView productImage;
            private TextView productName;
            private TextView productPrice;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.product_image);
                productName = itemView.findViewById(R.id.product_name);
                productPrice = itemView.findViewById(R.id.product_price);
            }
        }
    }

