export interface Customer {
  id: string;
  name: string;
  email: string;
  phone?: string;
  address?: string;
  createdAt: string;
  updatedAt: string;
  active: boolean;
}

export interface CreateCustomerRequest {
  name: string;
  email: string;
  phone?: string;
  address?: string;
}

export interface UpdateCustomerRequest {
  name: string;
  email: string;
  phone?: string;
  address?: string;
}
