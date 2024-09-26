export interface OrderResponse {
  id: string;
  orderNumber?: string;
  paymentServiceNumber?: string;
  createDate?: string;
  studentId: number;
  courseId: string;
  status: string;
  totalAmount: string;
}

export class Order implements OrderResponse {
  constructor(
    public id: string,
    public orderNumber: string,
    public paymentServiceNumber: string,
    public createDate: string,
    public studentId: number,
    public courseId: string,
    public status: string,
    public totalAmount: string
  ) {}
}
